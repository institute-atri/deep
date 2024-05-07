package org.instituteatri.deep.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.instituteatri.deep.model.token.Token;
import org.instituteatri.deep.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CustomLogoutHandlerTest {

    @Mock
    private SecurityFilter securityFilter;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private CustomLogoutHandler customLogoutHandler;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Logout: Valid Token - Invalidates Token and Logs")
    void logout_ValidToken_InvalidatesTokenAndLogs() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);
        String token = "validToken";
        when(securityFilter.recoverTokenFromRequest(request)).thenReturn(token);
        when(tokenRepository.findByTokenValue(token)).thenReturn(java.util.Optional.of(new Token()));

        customLogoutHandler.logout(request, response, authentication);

        verify(tokenRepository, times(1)).findByTokenValue(token);
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    @DisplayName("Logout: Invalid Token - Sends Unauthorized Response")
    void logout_InvalidToken_SendsUnauthorizedResponse() throws Exception {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);
        String token = "invalidToken";
        when(securityFilter.recoverTokenFromRequest(request)).thenReturn(token);
        when(tokenRepository.findByTokenValue(token)).thenReturn(java.util.Optional.empty());

        // Use StringWriter to capture the output
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Act
        customLogoutHandler.logout(request, response, authentication);

        // Assert
        verify(tokenRepository, times(1)).findByTokenValue(token);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // Assert the content written to the PrintWriter
        assertEquals("Token is expired", stringWriter.toString());
    }

    @Test
    @DisplayName("Send Unauthorized Response: Writes Unauthorized Response")
    void sendUnauthorizedResponse_WritesUnauthorizedResponse() throws IOException {
        // Arrange
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // Act
        customLogoutHandler.sendUnauthorizedResponse(mockResponse);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED.value(), mockResponse.getStatus());
        assertEquals("Token is expired", mockResponse.getContentAsString());
    }
}