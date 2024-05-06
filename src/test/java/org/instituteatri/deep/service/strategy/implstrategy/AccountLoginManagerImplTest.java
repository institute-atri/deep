package org.instituteatri.deep.service.strategy.implstrategy;

import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.model.user.UserRole;
import org.instituteatri.deep.exception.user.CustomAuthenticationException;
import org.instituteatri.deep.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountLoginManagerImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountLoginManagerImpl accountLoginManager;

    @Value("${emailTest}")
    private String emailTest;

    @Value("${passwordTest}")
    private String passwordTest;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }


    @Test
    void testHandleSuccessfulLogin_LockedAccount_ThrowsException() {
        // Arrange
        User user = mockLockedUser();

        // Act & Assert
        assertThrows(LockedException.class, () -> accountLoginManager.handleSuccessfulLoginComponent(user));
    }

    @Test
    void handleSuccessfulLoginComponent_accountEnabled_noExceptionThrown() {
        // Arrange
        User newUser = createUser(emailTest, passwordTest);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        accountLoginManager.handleSuccessfulLoginComponent(newUser);

        // Assert
        verify(userRepository).save(any(User.class));
    }


    @Test
    void testHandleBadCredentials_IncrementFailedLoginAttempts_WhenUserExists() {
        // Arrange
        User user = createUser(emailTest, passwordTest);
        user.setFailedLoginAttempts(0);
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        // Act
        assertThrows(CustomAuthenticationException.class, () -> accountLoginManager.handleBadCredentialsComponent("user@example.com"));

        // Assert
        assertEquals(1, user.getFailedLoginAttempts());
    }


    @Test
    void testHandleBadCredentials_LockAccount_WhenFailedLoginAttemptsExceedThreshold() {
        // Arrange
        User user = createUser(emailTest, passwordTest);
        user.setFailedLoginAttempts(3);
        when(userRepository.findByEmail(emailTest)).thenReturn(user);

        // Act
        assertThrows(CustomAuthenticationException.class, () -> accountLoginManager.handleBadCredentialsComponent(emailTest));

        // Assert
        verify(userRepository).findByEmail(emailTest);
        assertEquals(4, user.getFailedLoginAttempts());
    }

    private User mockLockedUser() {
        User user = new User();
        user.setEnable(false);
        user.setFailedLoginAttempts(3);
        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        return user;
    }

    private User createUser(String email, String password) {
        return new User(
                "user",
                email,
                password,
                true,
                UserRole.USER
        );
    }
}