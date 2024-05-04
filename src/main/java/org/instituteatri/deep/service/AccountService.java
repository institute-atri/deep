package org.instituteatri.deep.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.instituteatri.deep.domain.user.User;
import org.instituteatri.deep.domain.user.UserRole;
import org.instituteatri.deep.dtos.user.AuthenticationDTO;
import org.instituteatri.deep.dtos.user.RefreshTokenDTO;
import org.instituteatri.deep.dtos.user.RegisterDTO;
import org.instituteatri.deep.dtos.user.ResponseDTO;
import org.instituteatri.deep.infrastructure.exceptions.user.EmailAlreadyExistsException;
import org.instituteatri.deep.infrastructure.exceptions.user.PasswordsNotMatchException;
import org.instituteatri.deep.infrastructure.security.TokenService;
import org.instituteatri.deep.repositories.UserRepository;
import org.instituteatri.deep.service.components.authcomponents.AccountLoginComponent;
import org.instituteatri.deep.service.components.authcomponents.AccountTokenComponent;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AccountTokenComponent accountTokenComponent;
    private final AccountLoginComponent accountLoginComponent;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public ResponseEntity<ResponseDTO> loginAccount(AuthenticationDTO authDto, AuthenticationManager authManager) {

        try {
            var authResult = accountLoginComponent.authenticateUserComponent(authDto, authManager);
            var user = (User) authResult.getPrincipal();

            accountLoginComponent.handleSuccessfulLoginComponent(user);
            accountTokenComponent.revokeAllUserTokens(user);

            log.info("[USER_AUTHENTICATED] User: {} successfully authenticated.", user.getUsername());
            return ResponseEntity.ok(accountTokenComponent.generateTokenResponse(user));

        } catch (LockedException e) {
            log.warn("[USER_LOCKED] User account locked: {}", authDto.email());
            return accountLoginComponent.handleLockedAccountComponent();

        } catch (BadCredentialsException e) {
            log.warn("[USER_LOGIN_FAILED] Failed login attempt with email: {}", authDto.email());
            return accountLoginComponent.handleBadCredentialsComponent(authDto.email());
        }
    }

    public ResponseEntity<ResponseDTO> registerAccount(RegisterDTO registerDTO) {

        if (isEmailExists(registerDTO.email())) {
            log.warn("[EMAIL_EXISTS] E-mail not available: {}", registerDTO.email());
            throw new EmailAlreadyExistsException();
        }

        if (!registerDTO.password().equals(registerDTO.confirmPassword())) {
            log.warn("[PASSWORDS_NOT_MATCH] Passwords do not match for email: {}", registerDTO.email());
            throw new PasswordsNotMatchException();
        }
        User newUser = createUser(registerDTO);
        User savedUser = userRepository.save(newUser);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        log.info("[USER_REGISTERED] User registered successfully: {}", savedUser.getEmail());
        return ResponseEntity.created(uri).body(accountTokenComponent.generateTokenResponse(savedUser));
    }

    public ResponseEntity<ResponseDTO> refreshToken(RefreshTokenDTO refreshTokenDTO) {
        try {
            String userEmail = tokenService.validateToken(refreshTokenDTO.refreshToken());
            UserDetails userDetails = loadUserByUsername(userEmail);

            var user = (User) userDetails;
            accountTokenComponent.revokeAllUserTokens(user);

            log.info("[TOKEN_REFRESHED] Token refreshed successfully for user: {}", user.getEmail());
            return ResponseEntity.ok(accountTokenComponent.generateTokenResponse(user));

        } catch (Exception e) {
            log.error("[TOKEN_REFRESH_FAILED] Error processing token refresh request: {}", e.getMessage());
            return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isEmailExists(String email) {
        return loadUserByUsername(email) != null;
    }

    private User createUser(RegisterDTO registerDTO) {
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
