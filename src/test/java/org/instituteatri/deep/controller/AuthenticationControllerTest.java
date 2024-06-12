package org.instituteatri.deep.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.AssertionsForClassTypes;
import org.instituteatri.deep.dto.request.LoginRequestDTO;
import org.instituteatri.deep.dto.request.RefreshTokenRequestDTO;
import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AccountService accountService;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    CsrfToken csrfToken;

    @Mock
    Authentication authentication;

    @InjectMocks
    private AuthenticationController authenticationController;

    TokenResponseDTO tokenResponseDTO = new TokenResponseDTO("token", "refreshToken");

    @Nested
    @DisplayName("Tests Login Method")
    class testLoginMethod {

        @Test
        @DisplayName("Should return login success")
        void shouldReturnLoginSuccess() {
            // Arrange
            LoginRequestDTO loginRequestSuccess = new LoginRequestDTO("test@localhost.com", "@Test123k+");
            when(accountService.loginAccount(loginRequestSuccess, authenticationManager)).thenReturn(
                    ResponseEntity.ok(tokenResponseDTO));

            // Act
            ResponseEntity<TokenResponseDTO> responseEntityLoginSuccess = authenticationController.getLogin(loginRequestSuccess);

            // Assert
            AssertionsForClassTypes.assertThat(responseEntityLoginSuccess.getBody()).isEqualTo(tokenResponseDTO);
            AssertionsForClassTypes.assertThat(responseEntityLoginSuccess.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(accountService).loginAccount(loginRequestSuccess, authenticationManager);
        }

        @Test
        @DisplayName("Should return login failure")
        void shouldReturnLoginFailure() {
            // Arrange
            LoginRequestDTO loginRequestFailure = new LoginRequestDTO("test@localhost.com", "@Test123k+");
            when(accountService.loginAccount(loginRequestFailure, authenticationManager)).thenReturn(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

            // Act
            ResponseEntity<TokenResponseDTO> responseEntityLoginFailure = authenticationController.getLogin(loginRequestFailure);

            // Assert
            AssertionsForClassTypes.assertThat(responseEntityLoginFailure.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            verify(accountService).loginAccount(loginRequestFailure, authenticationManager);
        }
    }

    @Nested
    @DisplayName("Tests Register Method")
    class testRegisterMethod {

        RegisterRequestDTO registerRequest = new RegisterRequestDTO(
                "test",
                "test@localhost.com",
                "@Test123k+",
                "@Test123k+");

        @Test
        @DisplayName("Should return registration success")
        void shouldReturnRegistrationSuccess() {
            // Arrange
            when(accountService.registerAccount(registerRequest)).thenReturn(
                    ResponseEntity.status(HttpStatus.OK).build());

            // Act
            ResponseEntity<TokenResponseDTO> responseEntityRegister = authenticationController.register(registerRequest);

            // Assert
            AssertionsForClassTypes.assertThat(responseEntityRegister.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(accountService).registerAccount(registerRequest);
        }

        @Test
        @DisplayName("Should return registration failure")
        void shouldReturnRegistrationFailure() {
            // Arrange
            when(accountService.registerAccount(registerRequest)).thenReturn(
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

            // Act
            ResponseEntity<TokenResponseDTO> responseEntity = authenticationController.register(registerRequest);

            // Assert
            AssertionsForClassTypes.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            verify(accountService).registerAccount(registerRequest);
        }
    }

    @Nested
    @DisplayName("Tests Logout Method")
    class testLogoutMethod {

        @Test
        @DisplayName("Should return logout success")
        void shouldReturnLogoutSuccess() {
            // Act
            ResponseEntity<Void> responseEntitySuccess = authenticationController.logout(request, response, authentication);

            // Assert
            AssertionsForClassTypes.assertThat(responseEntitySuccess.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @Nested
    @DisplayName("Tests RefreshToken Method")
    class testRefreshTokenMethod {

        RefreshTokenRequestDTO refreshTokenRequest = new RefreshTokenRequestDTO("refreshToken");

        @Test
        @DisplayName("Should return refreshToken success")
        void shouldReturnRefreshTokenSuccess() {
            // Arrange
            when(accountService.refreshToken(refreshTokenRequest)).thenReturn(
                    ResponseEntity.ok(tokenResponseDTO));

            // Act
            ResponseEntity<TokenResponseDTO> responseEntityTokenDTO = authenticationController.refreshToken(refreshTokenRequest);

            // Assert
            AssertionsForClassTypes.assertThat(responseEntityTokenDTO.getBody()).isEqualTo(tokenResponseDTO);
            AssertionsForClassTypes.assertThat(responseEntityTokenDTO.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(accountService).refreshToken(refreshTokenRequest);
        }

        @Test
        @DisplayName("Should return refreshToken failure")
        void shouldReturnRefreshTokenFailure() {
            // Arrange
            when(accountService.refreshToken(refreshTokenRequest))
                    .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

            // Act
            ResponseEntity<TokenResponseDTO> responseEntity = authenticationController.refreshToken(refreshTokenRequest);

            // Assert
            AssertionsForClassTypes.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            verify(accountService).refreshToken(refreshTokenRequest);
        }
    }

    @Test
    @DisplayName("Should Return CsrfToken")
    void shouldReturnCsrfToken() {
        // Arrange
        when(request.getAttribute(CsrfToken.class.getName())).thenReturn(csrfToken);

        // Act
        CsrfToken returnedCsrfToken = authenticationController.csrf(request);

        // Assert
        assertEquals(csrfToken, returnedCsrfToken);
    }
}
