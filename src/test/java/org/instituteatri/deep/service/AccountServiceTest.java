package org.instituteatri.deep.service;

import org.instituteatri.deep.dto.request.LoginRequestDTO;
import org.instituteatri.deep.dto.request.RefreshTokenRequestDTO;
import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.exception.user.AccountLockedException;
import org.instituteatri.deep.exception.user.EmailAlreadyExistsException;
import org.instituteatri.deep.infrastructure.security.TokenService;
import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.model.user.UserRole;
import org.instituteatri.deep.repository.UserRepository;
import org.instituteatri.deep.service.strategy.interfaces.AccountLoginManager;
import org.instituteatri.deep.service.strategy.interfaces.AuthenticationTokenManager;
import org.instituteatri.deep.service.strategy.interfaces.PasswordValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    @DisplayName("Upon successful login, should return an OK response")
    void loginAccount_Success() {
        // Arrange
        LoginRequestDTO authDto = new LoginRequestDTO(emailTest, passwordTest);
        User user = new User();
        mockSuccessfulLogin(user);

        // Act
        ResponseEntity<TokenResponseDTO> response = accountService.loginAccount(authDto, authManager);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(accountLoginManager, times(1)).handleSuccessfulLogin(user);
    }

    @Test
    @DisplayName("When you try to log in with a locked account, you should throw an AccountLockedException exception")
    void loginAccount_AccountLocked() {
        // Arrange
        LoginRequestDTO authDto = new LoginRequestDTO(emailTest, passwordTest);
        mockLockedAccount();

        // Act & Assert
        assertThrows(AccountLockedException.class, () -> accountService.loginAccount(authDto, authManager));
    }

    @Test
    @DisplayName("When trying to log in with invalid credentials, it should return a null response and call handleBadCredentials")
    void loginAccount_BadCredentials() {
        // Arrange
        LoginRequestDTO authDto = new LoginRequestDTO(emailTest, passwordTest);
        mockBadCredentials();

        // Act
        ResponseEntity<TokenResponseDTO> response = accountService.loginAccount(authDto, authManager);

        // Assert
        assertNull(response, "Response entity should be null");
        verify(accountLoginManager, times(1)).handleBadCredentials(authDto.email());
    }

    @Test
    @DisplayName("When registering an account successfully, it should return a CREATED response and save the user in the repository")
    void registerAccount_Success() {

        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
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

        mockSuccessfulRegistration(registerRequestDTO, newUser);

        ResponseEntity<TokenResponseDTO> responseEntity = accountService.registerAccount(registerRequestDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertNotNull(Objects.requireNonNull(responseEntity.getBody()).token());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("When you try to register an account with an email that already exists, you should throw an EmailAlreadyExistsException")
    void registerAccount_EmailAlreadyExists() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "Test User",
                "test@example.com",
                "Password123+",
                "Password123+");

        when(userRepository.findByEmail(anyString())).thenReturn(new User());

        assertThrows(EmailAlreadyExistsException.class, () -> accountService.registerAccount(registerRequestDTO));
    }

    @Test
    @DisplayName("When updating the token, it should return an OK response")
    void testRefreshToken() {
        // Given
        String email = "refreshToken@test.com";
        RefreshTokenRequestDTO refreshTokenRequestDTO = new RefreshTokenRequestDTO("testToken");
        User testUser = new User();
        testUser.setEmail(email);

        // When
        when(tokenService.validateToken(refreshTokenRequestDTO.refreshToken())).thenReturn(email);
        when(accountService.loadUserByUsername(email)).thenReturn(testUser);
        when(authTokenManager.generateTokenResponse(testUser)).thenReturn(new ResponseEntity<TokenResponseDTO>(HttpStatus.OK).getBody());

        // Then
        ResponseEntity<TokenResponseDTO> responseEntity = accountService.refreshToken(refreshTokenRequestDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    private void mockSuccessfulLogin(User user) {
        Authentication authResult = mock(Authentication.class);
        when(authResult.getPrincipal()).thenReturn(user);
        when(accountLoginManager.authenticateUser(any(LoginRequestDTO.class), any(AuthenticationManager.class))).thenReturn(authResult);
        when(authTokenManager.generateTokenResponse(any(User.class))).thenReturn(new TokenResponseDTO("Token", "RefreshToken"));
    }

    private void mockLockedAccount() {
        when(accountLoginManager.authenticateUser(any(LoginRequestDTO.class), any(AuthenticationManager.class))).thenThrow(new LockedException("Account is locked"));
    }

    private void mockBadCredentials() {
        when(accountLoginManager.authenticateUser(any(LoginRequestDTO.class), any(AuthenticationManager.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));
    }

    private void mockSuccessfulRegistration(RegisterRequestDTO registerRequestDTO, User newUser) {
        doNothing().when(passwordValidationStrategy).validate(registerRequestDTO.password(), registerRequestDTO.confirmPassword());
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(authTokenManager.generateTokenResponse(any(User.class))).thenReturn(new TokenResponseDTO("Token", "RefreshToken"));
    }
}