package org.instituteatri.deep.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.instituteatri.deep.domain.user.User;
import org.instituteatri.deep.domain.user.UserRole;
import org.instituteatri.deep.dtos.user.AuthenticationDTO;
import org.instituteatri.deep.dtos.user.RefreshTokenDTO;
import org.instituteatri.deep.dtos.user.RegisterDTO;
import org.instituteatri.deep.dtos.user.ResponseDTO;
import org.instituteatri.deep.infrastructure.exceptions.user.AccountLockedException;
import org.instituteatri.deep.infrastructure.exceptions.user.EmailAlreadyExistsException;
import org.instituteatri.deep.infrastructure.exceptions.user.UserEmailNotFoundException;
import org.instituteatri.deep.infrastructure.security.TokenService;
import org.instituteatri.deep.repositories.UserRepository;
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

    public ResponseEntity<ResponseDTO> loginAccount(AuthenticationDTO authDto, AuthenticationManager authManager) {

        try {
            var authResult = accountLoginManager.authenticateUserComponent(authDto, authManager);
            var user = (User) authResult.getPrincipal();

            accountLoginManager.handleSuccessfulLoginComponent(user);
            authTokenManager.revokeAllUserTokens(user);

            log.info("[USER_AUTHENTICATED] User: {} successfully authenticated.", user.getUsername());
            return ResponseEntity.ok(authTokenManager.generateTokenResponse(user));

        } catch (LockedException e) {
            log.warn("[USER_LOCKED] User account locked: {}", authDto.email());
            throw new AccountLockedException();

        } catch (BadCredentialsException e) {
            log.warn("[USER_LOGIN_FAILED] Failed login attempt with email: {}", authDto.email());
            return accountLoginManager.handleBadCredentialsComponent(authDto.email());
        }
    }

    public ResponseEntity<ResponseDTO> registerAccount(RegisterDTO registerDTO) {

        if (isEmailExists(registerDTO.email())) {
            log.warn("[EMAIL_EXISTS] E-mail not available: {}", registerDTO.email());
            throw new EmailAlreadyExistsException();
        }

        passwordValidationStrategy.validate(registerDTO.password(), registerDTO.confirmPassword());

        User newUser = buildUserFromRegistrationDto(registerDTO);
        User savedUser = userRepository.save(newUser);
        String baseUri = "http://localhost:8080";

        URI uri = UriComponentsBuilder.fromUriString(baseUri)
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        log.info("[USER_REGISTERED] User registered successfully: {}", savedUser.getEmail());
        return ResponseEntity.created(uri).body(authTokenManager.generateTokenResponse(savedUser));
    }

    public ResponseEntity<ResponseDTO> refreshToken(RefreshTokenDTO refreshTokenDTO) {

        String userEmail = tokenService.validateToken(refreshTokenDTO.refreshToken());

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

    private User buildUserFromRegistrationDto(RegisterDTO registerDTO) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        return new User(
                registerDTO.name(),
                registerDTO.email(),
                encryptedPassword,
                true,
                UserRole.USER
        );
    }
}
