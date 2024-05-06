package org.instituteatri.deep.service;

import org.instituteatri.deep.domain.user.User;
import org.instituteatri.deep.domain.user.UserRole;
import org.instituteatri.deep.dtos.user.AuthenticationDTO;
import org.instituteatri.deep.dtos.user.RefreshTokenDTO;
import org.instituteatri.deep.dtos.user.RegisterDTO;
import org.instituteatri.deep.dtos.user.ResponseDTO;
import org.instituteatri.deep.infrastructure.exceptions.user.AccountLockedException;
import org.instituteatri.deep.infrastructure.exceptions.user.EmailAlreadyExistsException;
import org.instituteatri.deep.infrastructure.security.TokenService;
import org.instituteatri.deep.repositories.UserRepository;
import org.instituteatri.deep.service.strategy.interfaces.AccountLoginManager;
import org.instituteatri.deep.service.strategy.interfaces.AuthenticationTokenManager;
import org.instituteatri.deep.service.strategy.interfaces.PasswordValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountServiceTest {

    @Mock
    private AccountLoginManager accountLoginManager;
    @Mock
    private AuthenticationTokenManager authTokenManager;
    @Mock
    private PasswordValidationStrategy passwordValidationStrategy;
    @Mock
    private TokenService tokenService;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    @Value("${emailTest}")
    private String emailTest;

    @Value("${passwordTest}")
    private String passwordTest;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void loginAccount_Success() {
        // Arrange
        AuthenticationDTO authDto = new AuthenticationDTO(emailTest, passwordTest);
        User user = new User();
        mockSuccessfulLogin(user);

        // Act
        ResponseEntity<ResponseDTO> response = accountService.loginAccount(authDto, authManager);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(accountLoginManager, times(1)).handleSuccessfulLoginComponent(user);
    }

    @Test
    void loginAccount_AccountLocked() {
        // Arrange
        AuthenticationDTO authDto = new AuthenticationDTO(emailTest, passwordTest);
        mockLockedAccount();

        // Act & Assert
        assertThrows(AccountLockedException.class, () -> accountService.loginAccount(authDto, authManager));
    }

    @Test
    void loginAccount_BadCredentials() {
        // Arrange
        AuthenticationDTO authDto = new AuthenticationDTO(emailTest, passwordTest);
        mockBadCredentials();

        // Act
        ResponseEntity<ResponseDTO> response = accountService.loginAccount(authDto, authManager);

        // Assert
        assertNull(response, "Response entity should be null");
        verify(accountLoginManager, times(1)).handleBadCredentialsComponent(authDto.email());
    }

    @Test
    void registerAccount_Success() {

        RegisterDTO registerDTO = new RegisterDTO(
                "Test User",
                "test@example.com",
                "Password123+",
                "Password123+");

        User newUser = new User(
                "Test User",
                "test@example.com",
                new BCryptPasswordEncoder().encode("Password123+"),
                true,
                UserRole.USER);

        mockSuccessfulRegistration(registerDTO, newUser);

        ResponseEntity<ResponseDTO> response = accountService.registerAccount(registerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).token());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerAccount_EmailAlreadyExists() {
        RegisterDTO registerDTO = new RegisterDTO(
                "Test User",
                "test@example.com",
                "Password123+",
                "Password123+");

        when(userRepository.findByEmail(anyString())).thenReturn(new User());

        assertThrows(EmailAlreadyExistsException.class, () -> accountService.registerAccount(registerDTO));
    }

    @Test
    void testRefreshToken() {
        // Given
        String email = "refreshToken@test.com";
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO("testToken");
        User testUser = new User();
        testUser.setEmail(email);

        // When
        when(tokenService.validateToken(refreshTokenDTO.refreshToken())).thenReturn(email);
        when(accountService.loadUserByUsername(email)).thenReturn(testUser);
        when(authTokenManager.generateTokenResponse(testUser)).thenReturn(new ResponseEntity<ResponseDTO>(HttpStatus.OK).getBody());

        // Then
        ResponseEntity<ResponseDTO> response = accountService.refreshToken(refreshTokenDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    private void mockSuccessfulLogin(User user) {
        Authentication authResult = mock(Authentication.class);
        when(authResult.getPrincipal()).thenReturn(user);
        when(accountLoginManager.authenticateUserComponent(any(AuthenticationDTO.class), any(AuthenticationManager.class))).thenReturn(authResult);
        when(authTokenManager.generateTokenResponse(any(User.class))).thenReturn(new ResponseDTO("Token", "RefreshToken"));
    }

    private void mockLockedAccount() {
        when(accountLoginManager.authenticateUserComponent(any(AuthenticationDTO.class), any(AuthenticationManager.class))).thenThrow(new LockedException("Account is locked"));
    }

    private void mockBadCredentials() {
        when(accountLoginManager.authenticateUserComponent(any(AuthenticationDTO.class), any(AuthenticationManager.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));
    }

    private void mockSuccessfulRegistration(RegisterDTO registerDTO, User newUser) {
        doNothing().when(passwordValidationStrategy).validate(registerDTO.password(), registerDTO.confirmPassword());
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(authTokenManager.generateTokenResponse(any(User.class))).thenReturn(new ResponseDTO("Token", "RefreshToken"));
    }
}