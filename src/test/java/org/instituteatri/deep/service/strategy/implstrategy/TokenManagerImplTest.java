package org.instituteatri.deep.service.strategy.implstrategy;

import org.instituteatri.deep.model.token.Token;
import org.instituteatri.deep.model.token.TokenType;
import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.model.user.UserRole;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.infrastructure.security.TokenService;
import org.instituteatri.deep.repository.TokenRepository;
import org.instituteatri.deep.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class TokenManagerImplTest {

    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserRepository userRepository;
    private final String tokenValue = "sampleTokenValue";

    @InjectMocks
    private TokenManagerImpl tokenManager;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void generateTokenResponse_shouldGenerateAndReturnTokens() {
        // Arrange
        User user = new User(
                "Test User",
                "test@example.com",
                new BCryptPasswordEncoder().encode("Password123+"),
                true,
                UserRole.USER);

        String genAccessToken = "sampleAccessToken";
        String genRefreshToken = "sampleRefreshToken";
        Token accessToken = new Token();
        Token refreshToken = new Token();
        accessToken.setTokenValue(genAccessToken);
        refreshToken.setTokenValue(genRefreshToken);

        when(tokenService.generateAccessToken(user)).thenReturn(genAccessToken);
        when(tokenService.generateRefreshToken(user)).thenReturn(genRefreshToken);
        when(tokenRepository.findAllByUserId(user.getId())).thenReturn(Collections.emptyList());
        when(tokenRepository.save(any(Token.class))).thenReturn(accessToken, refreshToken);

        // Act
        TokenResponseDTO tokenResponseDTO = tokenManager.generateTokenResponse(user);

        // Assert
        assertEquals(genAccessToken, tokenResponseDTO.token());
        assertEquals(genRefreshToken, tokenResponseDTO.refreshToken());
    }

    @Test
    void generateTokenResponse_shouldThrowExceptionWhenTokenServiceFails() {
        // Given
        User user = new User();
        when(tokenService.generateAccessToken(user)).thenThrow(new RuntimeException("TokenService failed"));

        // When & Then
        assertThrows(RuntimeException.class, () -> tokenManager.generateTokenResponse(user));
    }

    @Test
    void generateTokenResponse_shouldThrowExceptionWhenTokenRepositoryFails() {
        // Given
        User user = new User();
        when(tokenService.generateAccessToken(user)).thenReturn("sampleAccessToken");
        when(tokenService.generateRefreshToken(user)).thenReturn("sampleRefreshToken");
        when(tokenRepository.findAllByUserId(user.getId())).thenThrow(new RuntimeException("TokenRepository failed"));

        // When & Then
        assertThrows(RuntimeException.class, () -> tokenManager.generateTokenResponse(user));
    }


    @Test
    void clearTokens_shouldDeleteAllTokensForUser() {
        // Arrange
        String userId = "testUserId";
        List<Token> tokens = new ArrayList<>();
        tokens.add(new Token());
        tokens.add(new Token());

        when(tokenRepository.findAllByUserId(userId)).thenReturn(tokens);

        // Act
        tokenManager.clearTokens(userId);

        // Assert
        verify(tokenRepository).findAllByUserId(userId);
        verify(tokenRepository).deleteAll(tokens);
    }

    @Test
    void saveUserToken_shouldSaveTokenWithCorrectValues() {
        // Given
        User user = new User();
        Token expectedToken = Token.builder()
                .user(user)
                .tokenValue(tokenValue)
                .tokenType(TokenType.BEARER)
                .tokenExpired(false)
                .tokenRevoked(false)
                .build();

        when(tokenRepository.save(expectedToken)).thenReturn(expectedToken);

        // When
        Token actualToken = tokenManager.saveUserToken(user, tokenValue);

        // Then
        assertEquals(expectedToken, actualToken);
    }

    @Test
    void saveUserToken_shouldThrowExceptionWhenTokenRepositoryFails() {
        // Given
        User user = new User();
        when(tokenRepository.save(any(Token.class))).thenThrow(new RuntimeException("TokenRepository failed"));

        // When & Then
        assertThrows(RuntimeException.class, () -> tokenManager.saveUserToken(user, tokenValue));
    }

    @Test
    void revokeAllUserTokens_shouldRevokeAllUserTokens() {
        // Arrange
        User user = new User();
        user.setId("123");

        Token token1 = new Token();
        token1.setTokenExpired(false);
        token1.setTokenRevoked(false);

        Token token2 = new Token();
        token2.setTokenExpired(false);
        token2.setTokenRevoked(false);

        List<Token> validTokens = Arrays.asList(token1, token2);

        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(validTokens);

        // Act
        tokenManager.revokeAllUserTokens(user);

        // Assert
        verify(tokenRepository).findAllValidTokenByUser(user.getId());
        verify(tokenRepository).saveAll(validTokens);
        validTokens.forEach(token -> {
            assert token.isTokenExpired();
            assert token.isTokenRevoked();
        });
    }

    @Test
    void revokeAllUserTokens_shouldThrowExceptionWhenTokenRepositoryFails() {
        // Given
        User user = new User();
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenThrow(new RuntimeException("TokenRepository failed"));

        // When & Then
        assertThrows(RuntimeException.class, () -> tokenManager.revokeAllUserTokens(user));
    }
}