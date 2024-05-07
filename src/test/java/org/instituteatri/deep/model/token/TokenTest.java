package org.instituteatri.deep.model.token;

import org.instituteatri.deep.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TokenTest {

    @Mock
    private User user;

    @InjectMocks
    private Token token;

    @Test
    @DisplayName("Test Token Creation")
    void testTokenCreation() {
        Token fakeToken = new Token();
        fakeToken.setId("fakeId");

        fakeToken.setTokenValue("fakeTokenValue");
        fakeToken.setTokenType(TokenType.BEARER);
        fakeToken.setTokenRevoked(false);
        fakeToken.setTokenExpired(false);
        fakeToken.setUser(null);

        assertNotNull(fakeToken.getId(), "Token ID should not be null");
        assertNotNull(fakeToken.getTokenValue(), "Token value should not be null");
        assertEquals(TokenType.BEARER, fakeToken.getTokenType(), "Token type should be 'BEARER'");
        assertFalse(fakeToken.isTokenRevoked(), "Token should not be revoked by default");
        assertFalse(fakeToken.isTokenExpired(), "Token should not be expired by default");
        assertNull(fakeToken.getUser(), "Token user should be null by default");
    }


    @Test
    @DisplayName("Test Token Type Setting")
    void testTokenType() {
        // Act
        token.setTokenType(TokenType.BEARER);

        // Assert
        assertEquals(TokenType.BEARER, token.getTokenType(), "Token type should be set correctly");
    }

    @Test
    @DisplayName("Test Token Revocation")
    void testTokenRevocation() {
        // Act
        token.setTokenRevoked(true);

        // Assert
        assertTrue(token.isTokenRevoked(), "Token should be revoked");
    }

    @Test
    @DisplayName("Test Token Expiration")
    void testTokenExpiration() {
        // Act
        token.setTokenExpired(true);

        // Assert
        assertTrue(token.isTokenExpired(), "Token should be expired");
    }
}
