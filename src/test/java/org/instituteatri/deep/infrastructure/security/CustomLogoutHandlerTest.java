package org.instituteatri.deep.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.instituteatri.deep.exception.user.TokenGenerationException;
import org.instituteatri.deep.model.token.Token;
import org.instituteatri.deep.repository.TokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomLogoutHandlerTest {

    @Mock
    private SecurityFilter securityFilter;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    PrintWriter writer;

    @InjectMocks
    private CustomLogoutHandler customLogoutHandler;

    String validToken = "validToken";
    String invalidToken = "invalidToken";
    String tokenExpiredMessage = "Token is expired";

    @Nested
    @DisplayName("Tests Logout Handler")
    class testLogoutHandler {

        @Test
        @DisplayName("Should logout successfully when valid token is provided")
        void shouldLogoutSuccessfullyWithValidToken() {
            // Arrange
            Token storedToken = new Token();

            when(securityFilter.recoverTokenFromRequest(request)).thenReturn(validToken);
            when(tokenRepository.findByTokenValue(validToken)).thenReturn(Optional.of(storedToken));

            // Act
            customLogoutHandler.logout(request, response, authentication);

            // Assert
            storedToken.setTokenExpired(true);
            storedToken.setTokenRevoked(true);
            verify(tokenRepository).save(storedToken);
            verify(securityFilter).recoverTokenFromRequest(request);
            SecurityContextHolder.clearContext();
        }


        @Test
        @DisplayName("Should handle logout with invalid token by returning unauthorized status")
        void shouldHandleLogoutWithInvalidTokenByReturningUnauthorizedStatus() throws IOException {
            // Arrange
            when(securityFilter.recoverTokenFromRequest(request)).thenReturn(invalidToken);
            when(tokenRepository.findByTokenValue(invalidToken)).thenReturn(Optional.empty());
            when(response.getWriter()).thenReturn(writer);

            // Act
            customLogoutHandler.logout(request, response, authentication);

            // Assert
            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(writer).write(tokenExpiredMessage);
        }

        @Test
        @DisplayName("Should handle logout with IOException by throwing TokenGenerationException")
        void shouldHandleLogoutWithIOExceptionByThrowingTokenGenerationException() throws IOException {
            // Arrange
            when(securityFilter.recoverTokenFromRequest(request)).thenReturn(invalidToken);
            when(tokenRepository.findByTokenValue(invalidToken)).thenReturn(Optional.empty());
            when(response.getWriter()).thenReturn(writer);
            doThrow(new IOException("IO error")).when(writer).write(tokenExpiredMessage);

            // Act & Assert
            assertThrows(TokenGenerationException.class, () ->
                    customLogoutHandler.logout(request, response, authentication));
        }
    }

    @Nested
    @DisplayName("Tests FindTokenByValue Method Tests")
    class FindTokenByValueTest {

        @Test
        @DisplayName("Should find token by value when token is found")
        void shouldFindTokenByValueWhenTokenFound() {
            // Arrange
            Token expectedToken = new Token();

            when(tokenRepository.findByTokenValue(validToken)).thenReturn(Optional.of(expectedToken));

            // Act
            Token actualToken = customLogoutHandler.findTokenByValue(validToken);

            // Assert
            assertSame(expectedToken, actualToken);
            verify(tokenRepository).findByTokenValue(validToken);
        }

        @Test
        @DisplayName("Should return null when token is not found")
        void shouldReturnNullWhenTokenNotFound() {
            // Arrange
            String tokenNotFound = "tokenNotFound";

            when(tokenRepository.findByTokenValue(tokenNotFound)).thenReturn(Optional.empty());

            // Act
            Token actualToken = customLogoutHandler.findTokenByValue(tokenNotFound);

            // Assert
            assertNull(actualToken);
            verify(tokenRepository).findByTokenValue(tokenNotFound);
        }
    }

    @Test
    @DisplayName("Should set expired and revoked flags to true and save token")
    void invalidateToken_shouldSetExpiredAndRevokedToTrueAndSaveToken() {
        // Arrange
        Token invalidateToken = new Token();

        // Act
        customLogoutHandler.invalidateToken(invalidateToken);

        // Assert
        assertTrue(invalidateToken.isTokenExpired(), "Token should be marked as expired");
        assertTrue(invalidateToken.isTokenRevoked(), "Token should be marked as revoked");
        verify(tokenRepository).save(invalidateToken);
    }

    @Test
    @DisplayName("Should set status and write message to response")
    void sendUnauthorizedResponse_shouldSetStatusAndWriteMessageToResponse() throws IOException {
        // Arrange
        when(response.getWriter()).thenReturn(writer);

        // Act
        customLogoutHandler.sendUnauthorizedResponse(response);

        // Assert
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(writer).write(tokenExpiredMessage);
    }
}