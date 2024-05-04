package org.instituteatri.deep.service.components.authcomponents;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.domain.user.User;
import org.instituteatri.deep.dtos.user.AuthenticationDTO;
import org.instituteatri.deep.dtos.user.ResponseDTO;
import org.instituteatri.deep.infrastructure.exceptions.user.AccountLockedException;
import org.instituteatri.deep.infrastructure.exceptions.user.CustomAuthenticationException;
import org.instituteatri.deep.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountLoginComponent {

    private final UserRepository userRepository;

    public Authentication authenticateUserComponent(AuthenticationDTO authDto, AuthenticationManager authManager) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
        return authManager.authenticate(usernamePassword);
    }

    public void handleSuccessfulLoginComponent(User user) {
        if (!user.isEnable()) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);
            if (user.getFailedLoginAttempts() >= 4) {
                user.lockAccountForHours();
                userRepository.save(user);
            }
            throw new LockedException("Account is locked.");
        }
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    public ResponseEntity<ResponseDTO> handleLockedAccountComponent() {
        throw new AccountLockedException();
    }

    public ResponseEntity<ResponseDTO> handleBadCredentialsComponent(String email) {
        var user = (User) userRepository.findByEmail(email);
        if (user != null) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);
            if (user.getFailedLoginAttempts() >= 4) {
                user.lockAccountForHours();
                userRepository.save(user);
            }
        }
        throw new CustomAuthenticationException();
    }
}
