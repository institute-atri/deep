package org.instituteatri.deep.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.instituteatri.deep.exception.user.TokenInvalidException;
import org.instituteatri.deep.model.token.Token;
import org.instituteatri.deep.repository.TokenRepository;
import org.instituteatri.deep.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SecurityFilterTest {

    @Mock
    private TokenService tokenService;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private SecurityFilter securityFilter;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Upon receiving a valid token, handleAuthentication must be called")
    void testDoFilterInternal_WithValidToken_ShouldCallHandleAuthentication() throws Exception {
        // Arrange
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        FilterChain filterChainMock = mock(FilterChain.class);

        // Simulate a valid token in the request
        when(requestMock.getHeader("Authorization")).thenReturn("Bearer validToken");

        // Act
        securityFilter.doFilterInternal(requestMock, responseMock, filterChainMock);

        // Assert
        verify(filterChainMock, times(1)).doFilter(requestMock, responseMock);
    }

    @Test
    @DisplayName("When receiving an authorization header present, must retrieve the token")
    void testRecoverTokenFromRequest_WhenAuthorizationHeaderPresent_ExpectTokenRecovered() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");

        // Act
        String recoveredToken = securityFilter.recoverTokenFromRequest(request);

        // Assert
        assertEquals("mockToken", recoveredToken);
    }

    @Test
    @DisplayName("When the authorization header is not present, the retrieved token is expected to be null")
    void testRecoverTokenFromRequest_WhenAuthorizationHeaderNotPresent_ExpectNull() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        String recoveredToken = securityFilter.recoverTokenFromRequest(request);

        // Assert
        assertNull(recoveredToken);
    }

    @Test
    @DisplayName("Upon receiving a valid token and user details, you must set authentication in the security context")
    void handleAuthentication_ValidTokenAndUserDetails_ShouldSetAuthenticationInSecurityContext() {
        mockValidTokenAndUserDetails();
        UserDetails userDetails = mock(UserDetails.class);
        assertAuthenticationSetInSecurityContext(userDetails);
    }

    @Test
    @DisplayName("Upon receiving an invalid token, it should handle the invalid token")
    void handleAuthentication_InvalidToken_ShouldHandleInvalidToken() throws Exception {
        // Arrange
        String token = "invalidToken";
        when(tokenService.validateToken(token)).thenThrow(new TokenInvalidException("Invalid token"));

        // Mock HttpServletResponse.getWriter() to return a valid PrintWriter
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // Act
        securityFilter.handleAuthentication(token, response);

        // Assert
        verify(tokenService, times(1)).validateToken(token);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Verify that the write method was called with the correct message
        verify(writer, times(1)).write("Token is invalid:Invalid token");
    }


    @Test
    @DisplayName("When the token is not revoked and is not expired, it should return true")
    void testIsTokenValid_WhenTokenIsNotRevokedAndNotExpired_ExpectTrue() {
        // Arrange
        String token = "validToken";
        Token tokenEntity = new Token();
        tokenEntity.setTokenRevoked(false);
        tokenEntity.setTokenExpired(false);
        when(tokenRepository.findByTokenValue(token)).thenReturn(Optional.of(tokenEntity));

        // Act
        boolean result = securityFilter.isTokenValid(token);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("When the token is revoked and expired, it should return false")
    void testIsTokenValid_WhenTokenIsRevokedAndExpired_ExpectFalse() {
        // Arrange
        String token = "revokedAndExpired";
        Token tokenEntity = new Token();
        tokenEntity.setTokenRevoked(true);
        tokenEntity.setTokenExpired(true);
        when(tokenRepository.findByTokenValue(token)).thenReturn(Optional.of(tokenEntity));

        // Act
        boolean result = securityFilter.isTokenValid(token);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("When the token is not found, it should return false")
    void testIsTokenValid_WhenTokenNotFound_ExpectFalse() {
        // Arrange
        String token = "nonExistentToken";
        when(tokenRepository.findByTokenValue(token)).thenReturn(Optional.empty());

        // Act
        boolean result = securityFilter.isTokenValid(token);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Set Authentication in Security Context")
    void setAuthenticationInSecurityContext() {
        UserDetails userDetails = mock(UserDetails.class);
        assertAuthenticationSetInSecurityContext(userDetails);
    }

    @Test
    @DisplayName("Handle Invalid Token: Should Set Status and Write Message")
    void handleInvalidToken_shouldSetStatusAndWriteMessage() throws IOException {
        // Arrange
        TokenInvalidException tokenInvalidException = new TokenInvalidException("Invalid token");
        PrintWriter writerMock = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writerMock);

        // Act
        securityFilter.handleInvalidToken(response, tokenInvalidException);

        // Assert
        verify(response, times(1)).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(writerMock, times(1)).write(tokenInvalidException.getMessage());
    }


    private void mockValidTokenAndUserDetails() {
        UserDetails userDetails = mock(UserDetails.class);
        Token tokenObject = mock(Token.class);
        when(tokenService.validateToken("validToken")).thenReturn("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(userDetails);
        when(tokenRepository.findByTokenValue("validToken")).thenReturn(Optional.of(tokenObject));
        when(tokenObject.isTokenExpired()).thenReturn(false);
        when(tokenObject.isTokenRevoked()).thenReturn(false);
    }

    private void assertAuthenticationSetInSecurityContext(UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        securityFilter.setAuthenticationInSecurityContext(userDetails);
        verify(securityContext, times(1)).setAuthentication(authentication);
    }
}