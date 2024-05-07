package org.instituteatri.deep.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.model.user.UserRole;
import org.instituteatri.deep.dto.request.LoginRequestDTO;
import org.instituteatri.deep.dto.request.RefreshTokenRequestDTO;
import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.exception.user.AccountLockedException;
import org.instituteatri.deep.exception.user.EmailAlreadyExistsException;
import org.instituteatri.deep.exception.user.UserEmailNotFoundException;
import org.instituteatri.deep.infrastructure.security.TokenService;
import org.instituteatri.deep.repository.UserRepository;
import org.instituteatri.deep.service.strategy.interfaces.AccountLoginManager;
import org.instituteatri.deep.service.strategy.interfaces.AuthenticationTokenManager;
import org.instituteatri.deep.service.strategy.interfaces.PasswordValidationStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthenticationTokenManager authTokenManager;
    private final AccountLoginManager accountLoginManager;
    private final PasswordValidationStrategy passwordValidationStrategy;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public ResponseEntity<TokenResponseDTO> loginAccount(LoginRequestDTO authDto, AuthenticationManager authManager) {

        try {
            var authResult = accountLoginManager.authenticateUser(authDto, authManager);
            var user = (User) authResult.getPrincipal();

            accountLoginManager.handleSuccessfulLogin(user);
            authTokenManager.revokeAllUserTokens(user);

            log.info("[USER_AUTHENTICATED] User: {} successfully authenticated.", user.getUsername());
            return ResponseEntity.ok(authTokenManager.generateTokenResponse(user));

        } catch (LockedException e) {
            log.warn("[USER_LOCKED] User account locked: {}", authDto.email());
            throw new AccountLockedException();

        } catch (BadCredentialsException e) {
            log.warn("[USER_LOGIN_FAILED] Failed login attempt with email: {}", authDto.email());
            return accountLoginManager.handleBadCredentials(authDto.email());
        }
    }

    public ResponseEntity<TokenResponseDTO> registerAccount(RegisterRequestDTO registerRequestDTO) {

        if (isEmailExists(registerRequestDTO.email())) {
            log.warn("[EMAIL_EXISTS] E-mail not available: {}", registerRequestDTO.email());
            throw new EmailAlreadyExistsException();
        }

        passwordValidationStrategy.validate(registerRequestDTO.password(), registerRequestDTO.confirmPassword());

        User newUser = buildUserFromRegistrationDto(registerRequestDTO);
        User savedUser = userRepository.save(newUser);
        String baseUri = "http://localhost:8080";

        URI uri = UriComponentsBuilder.fromUriString(baseUri)
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        log.info("[USER_REGISTERED] User registered successfully: {}", savedUser.getEmail());
        return ResponseEntity.created(uri).body(authTokenManager.generateTokenResponse(savedUser));
    }

    public ResponseEntity<TokenResponseDTO> refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {

        String userEmail = tokenService.validateToken(refreshTokenRequestDTO.refreshToken());

        if (userEmail == null || userEmail.isEmpty()) {
            log.error("[TOKEN_REFRESH_FAILED] Invalid user email found in token.");
            throw new UserEmailNotFoundException();
        }

        UserDetails userDetails = loadUserByUsername(userEmail);

        var user = (User) userDetails;
        authTokenManager.revokeAllUserTokens(user);

        log.info("[TOKEN_REFRESHED] Token refreshed successfully for user: {}", user.getEmail());
        return ResponseEntity.ok(authTokenManager.generateTokenResponse(user));
    }

    private boolean isEmailExists(String email) {
        return loadUserByUsername(email) != null;
    }

    private User buildUserFromRegistrationDto(RegisterRequestDTO registerRequestDTO) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequestDTO.password());
        return new User(
                registerRequestDTO.name(),
                registerRequestDTO.email(),
                encryptedPassword,
                true,
                UserRole.USER
        );
    }
}
