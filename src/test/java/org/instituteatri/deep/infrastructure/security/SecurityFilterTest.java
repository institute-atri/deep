package org.instituteatri.deep.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.instituteatri.deep.exception.user.TokenInvalidException;
import org.instituteatri.deep.model.token.Token;
import org.instituteatri.deep.repository.TokenRepository;
import org.instituteatri.deep.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Mock
    private FilterChain filterChain;

    @Mock
    private PrintWriter mockWriter;

    @Mock
    private Token mockToken;

    @Mock
    private UserDetails mockUserDetails;

    @InjectMocks
    private SecurityFilter securityFilter;

    private final String invalidToken = "Invalid token";
    private final String validToken = "valid token";
    private final String email = "user@example.com";


    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("Tests Do Filter Internal parameters")
    class testDoFilterInternalParameters {
        @Test
        @DisplayName("Should throw NullPointerException when request is null")
        void testDoFilterInternal_withNullRequest() {
            // Act
            request = null;

            // Assert
            assertThrows(NullPointerException.class, () ->
                    securityFilter.doFilterInternal(request, response, filterChain)
            );
        }

        @Test
        @DisplayName("Should throw NullPointerException when response is null")
        void testDoFilterInternal_withNullResponse() {
            // Act
            response = null;

            // Assert
            assertThrows(NullPointerException.class, () ->
                    securityFilter.doFilterInternal(request, response, filterChain)
            );
        }

        @Test
        @DisplayName("Should throw NullPointerException when filterChain is null")
        void testDoFilterInternal_withNullFilterChain() {
            // Act
            filterChain = null;

            // Assert
            assertThrows(NullPointerException.class, () ->
                    securityFilter.doFilterInternal(request, response, filterChain)
            );
        }

        @Test
        @DisplayName("Should not throw exception when request is not null")
        void testDoFilterInternal_withNonNullRequest() {
            assertDoesNotThrow(() ->
                    securityFilter.doFilterInternal(request, response, filterChain)
            );
        }

        @Test
        @DisplayName("Should not throw exception when response is not null")
        void testDoFilterInternal_withNonNullResponse() {
            assertDoesNotThrow(() ->
                    securityFilter.doFilterInternal(request, response, filterChain)
            );
        }

        @Test
        @DisplayName("Should not throw exception when filterChain is not null")
        void testDoFilterInternal_withNonNullFilterChain() {
            assertDoesNotThrow(() ->
                    securityFilter.doFilterInternal(request, response, filterChain)
            );
        }
    }

    @Nested
    @DisplayName("Tests Do Filter Internal method")
    class testDoFilterInternalMethod {
        @Test
        @DisplayName("Should handle authentication for valid token")
        void testDoFilterInternal_ValidToken() throws Exception {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
            when(tokenService.validateToken(validToken)).thenReturn(email);

            // Act
            securityFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(tokenService, times(1)).validateToken(validToken);
            verify(filterChain, times(0)).doFilter(request, response);
        }

        @Test
        @DisplayName("Should not handle authentication for null token")
        void testDoFilterInternal_NullToken() throws Exception {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn(null);

            // Act
            securityFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(tokenService, never()).validateToken(anyString());
            verify(filterChain, times(1)).doFilter(request, response);
        }

        @Test
        @DisplayName("Should propagate exception when error occurs")
        void testDoFilterInternal_Exception() {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
            when(tokenService.validateToken(invalidToken)).thenThrow(new RuntimeException("Token validation failed"));

            // Act & Assert
            assertThrows(RuntimeException.class, () -> securityFilter.doFilterInternal(request, response, filterChain));
        }

        @Test
        @DisplayName("Should proceed without token")
        void testDoFilterInternal_withoutToken() throws ServletException, IOException {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn(null);

            // Act
            securityFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain).doFilter(request, response);
            verify(tokenService, never()).validateToken(any());
            verify(userRepository, never()).findByEmail(any());
            verify(tokenRepository, never()).findByTokenValue(any());
        }

        @Test
        @DisplayName("Should not filter request and set authentication when token is invalid")
        void testDoFilterInternal_withInvalidToken() throws ServletException, IOException {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
            when(tokenService.validateToken(invalidToken)).thenReturn(null);

            // Act
            securityFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, never()).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }
    }

    @Nested
    @DisplayName("Tests Handle Authentication method")
    class testHandleAuthenticationMethod {
        @Test
        @DisplayName("Should handle invalid token")
        void testHandleAuthentication_withInvalidToken_ShouldCallHandleInvalidToken() throws IOException {
            // Arrange
            when(tokenService.validateToken(invalidToken)).thenThrow(new TokenInvalidException(invalidToken));
            when(response.getWriter()).thenReturn(mockWriter);

            // Act
            securityFilter.handleAuthentication(invalidToken, response);

            // Assert
            verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
            verify(mockWriter).write("Token is invalid:" + invalidToken);
        }

        @Test
        @DisplayName("Should set authentication in security context when token and user details are valid")
        void testHandleAuthentication_withValidTokenAndUserDetails_ShouldSetAuthentication() throws IOException {
            // Arrange
            when(tokenService.validateToken(validToken)).thenReturn(email);
            when(userRepository.findByEmail(email)).thenReturn(mockUserDetails);
            when(mockUserDetails.getUsername()).thenReturn("testUser");
            when(tokenRepository.findByTokenValue(validToken)).thenReturn(Optional.of(mockToken));
            when(mockToken.isTokenExpired()).thenReturn(false);
            when(mockToken.isTokenRevoked()).thenReturn(false);

            // Act
            boolean result = securityFilter.handleAuthentication(validToken, response);

            // Assert
            assertTrue(result);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            assertNotNull(authentication);
            assertEquals("testUser", authentication.getName());
            verify(response, never()).setStatus(anyInt());
        }
    }

    @Nested
    @DisplayName("Tests Token Is Revoked And Expired")
    class testTokenIsRevokedAndExpired {
        @Test
        @DisplayName("Should not proceed with valid token when user not found")
        void testDoFilterInternal_ValidToken_UserNotFound() throws ServletException, IOException {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
            when(tokenService.validateToken(validToken)).thenReturn(email);
            when(userRepository.findByEmail(email)).thenReturn(null);
            when(tokenRepository.findByTokenValue(validToken)).thenReturn(Optional.of(mockToken));
            when(mockToken.isTokenExpired()).thenReturn(false);
            when(mockToken.isTokenRevoked()).thenReturn(false);

            // Act
            securityFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, never()).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        @DisplayName("Should not proceed with expired token")
        void testDoFilterInternal_withExpiredToken() throws ServletException, IOException {
            // Arrange
            String expiredToken = "expiredToken";

            when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredToken);
            when(tokenService.validateToken(expiredToken)).thenReturn(email);
            when(userRepository.findByEmail(email)).thenReturn(mockUserDetails);
            when(tokenRepository.findByTokenValue(expiredToken)).thenReturn(Optional.of(mockToken));
            when(mockToken.isTokenExpired()).thenReturn(true);

            // Act
            securityFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, never()).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        @DisplayName("Should not proceed with revoked token")
        void testDoFilterInternal_withRevokedToken() throws ServletException, IOException {
            // Arrange
            String revokedToken = "revokedToken";

            when(request.getHeader("Authorization")).thenReturn("Bearer " + revokedToken);
            when(tokenService.validateToken(revokedToken)).thenReturn(email);
            when(tokenRepository.findByTokenValue(revokedToken)).thenReturn(Optional.of(mockToken));
            when(mockToken.isTokenExpired()).thenReturn(false);
            when(mockToken.isTokenRevoked()).thenReturn(true);

            // Act
            securityFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, never()).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }
    }

    @Nested
    @DisplayName("Tests Is Token Valid method")
    class testIsTokenValidMethod {
        @Test
        @DisplayName("Token should be invalid if expired")
        void testIsTokenValid_whenTokenRepositoryReturnsExpiredToken_thenReturnsFalse() {
            // Arrange
            when(tokenRepository.findByTokenValue("expiredToken")).thenReturn(Optional.of(mockToken));
            when(mockToken.isTokenExpired()).thenReturn(true);

            // Act
            boolean result = securityFilter.isTokenValid("expiredToken");

            // Assert
            assertFalse(result, "The token should be invalid because it is expired");
            verify(tokenRepository, times(1)).findByTokenValue("expiredToken");
            verify(mockToken, times(1)).isTokenExpired();
        }

        @Test
        @DisplayName("Token should be invalid if revoked")
        void testIsTokenValid_whenTokenRepositoryReturnsRevokedToken_thenReturnsFalse() {
            // Arrange
            when(tokenRepository.findByTokenValue("revokedToken")).thenReturn(Optional.of(mockToken));
            when(mockToken.isTokenRevoked()).thenReturn(true);

            // Act
            boolean result = securityFilter.isTokenValid("revokedToken");

            // Assert
            assertFalse(result, "The token should be invalid because it is revoked");
            verify(tokenRepository, times(1)).findByTokenValue("revokedToken");
            verify(mockToken, times(1)).isTokenRevoked();
        }

        @Test
        @DisplayName("Token should be valid if not expired and not revoked")
        void testIsTokenValid_whenTokenRepositoryReturnsValidToken_thenReturnsTrue() {
            // Arrange
            when(tokenRepository.findByTokenValue(validToken)).thenReturn(Optional.of(mockToken));
            when(mockToken.isTokenExpired()).thenReturn(false);
            when(mockToken.isTokenRevoked()).thenReturn(false);

            // Act
            boolean result = securityFilter.isTokenValid(validToken);

            // Assert
            assertTrue(result, "The token should be valid because it is not expired and not revoked");
            verify(tokenRepository, times(1)).findByTokenValue(validToken);
            verify(mockToken, times(1)).isTokenExpired();
            verify(mockToken, times(1)).isTokenRevoked();
        }
    }

    @Nested
    @DisplayName("Tests Set Authentication in Security Context Method")
    class testSetAuthenticationInSecurityContextMethod {

        String username = "testUser";

        @Test
        @DisplayName("Should set authentication with empty authorities when authorities are null")
        public void shouldSetAuthenticationWithEmptyAuthorities_whenAuthoritiesAreNull() {
            // Given
            when(mockUserDetails.getUsername()).thenReturn(username);
            when(mockUserDetails.getAuthorities()).thenReturn(null);

            // When
            securityFilter.setAuthenticationInSecurityContext(mockUserDetails);

            // Then
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            assertNotNull(authentication);
            assertEquals(username, authentication.getName());
            assertEquals(Collections.emptyList(), authentication.getAuthorities());
        }

        @Test
        @DisplayName("Should set authentication with empty authorities when authorities are empty")
        public void shouldSetAuthenticationWithEmptyAuthorities_whenAuthoritiesAreEmpty() {
            // Arrange
            when(mockUserDetails.getUsername()).thenReturn(username);
            when(mockUserDetails.getAuthorities()).thenReturn(Collections.emptyList());

            // Act
            securityFilter.setAuthenticationInSecurityContext(mockUserDetails);

            // Assert
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            assertNotNull(authentication);
            assertEquals(username, authentication.getName());
            assertEquals(Collections.emptyList(), authentication.getAuthorities());
        }

        @Test
        @DisplayName("Should throw NullPointerException when UserDetails is null")
        public void shouldThrowNullPointerException_whenUserDetailsIsNull() {
            // Assert
            assertThrows(NullPointerException.class, () -> securityFilter.setAuthenticationInSecurityContext(null));
        }
    }
}