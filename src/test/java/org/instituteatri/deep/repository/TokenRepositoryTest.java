package org.instituteatri.deep.repository;

import jakarta.persistence.EntityManager;
import org.instituteatri.deep.model.token.Token;
import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.model.user.UserRole;
import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.instituteatri.deep.service.strategy.implstrategy.TokenManagerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@DataJpaTest
@ActiveProfiles("test")
class TokenRepositoryTest {

    @Mock
    TokenRepository tokenRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TokenManagerImpl tokenManager;

    @Autowired
    EntityManager entityManager;

    @Value("${emailTest}")
    private String emailTest;

    @Value("${passwordTest}")
    private String passwordTest;

    private final String userId = "user_id";

    @Test
    @DisplayName("Should find all valid tokens by user ID")
    void findAllValidTokenByUser() {
        // Given
        List<Token> tokens = Collections.singletonList(new Token());
        mockFindAllValidTokenByUser(tokens);

        // When
        List<Token> result = tokenRepository.findAllValidTokenByUser(userId);

        // Then
        assertEquals(tokens, result);
        verify(tokenRepository, times(1)).findAllValidTokenByUser(userId);
    }

    @Test
    @DisplayName("Should find all tokens by user ID")
    void findAllByUserId() {
        // Given
        List<Token> expectedTokens = Collections.emptyList();
        mockFindAllByUserId(expectedTokens);

        // When
        List<Token> actualTokens = tokenRepository.findAllByUserId(userId);

        // Then
        assertEquals(expectedTokens, actualTokens);
    }

    @Test
    @DisplayName("Should find token by value")
    void findByTokenValue() {
        // Given
        String tokenValue = "token_value";
        Token expectedToken = new Token();
        expectedToken.setId("token_id");
        expectedToken.setTokenValue(tokenValue);
        expectedToken.setTokenExpired(false);
        expectedToken.setTokenRevoked(false);
        mockFindByTokenValue(tokenValue, expectedToken);

        // When
        Optional<Token> actualToken = tokenRepository.findByTokenValue(tokenValue);

        // Then
        assertTrue(actualToken.isPresent());
        assertEquals(expectedToken, actualToken.get());
    }

    @Test
    @DisplayName("Should not find token by invalid value")
    void findByInvalidTokenValue() {
        // Given
        String invalidTokenValue = "invalid_token_value";
        mockFindByTokenValue(invalidTokenValue, null);

        // When
        Optional<Token> actualToken = tokenRepository.findByTokenValue(invalidTokenValue);

        // Then
        assertTrue(actualToken.isEmpty());
    }

    @Test
    @DisplayName("Should delete tokens associated with a user from the database")
    void deleteTokensByUser() {
        // Given
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "Test",
                emailTest,
                passwordTest,
                passwordTest
        );
        User existingUser = createUser(registerRequestDTO);
        mockDeleteTokensByUser(existingUser);

        // When
        tokenManager.revokeAllUserTokens(existingUser);

        // Then
        verify(tokenRepository, times(1)).findAllValidTokenByUser(existingUser.getId());
        verify(tokenRepository, never()).saveAll(anyList());
    }

    private void mockFindAllValidTokenByUser(List<Token> tokens) {
        when(tokenRepository.findAllValidTokenByUser(userId)).thenReturn(tokens);
    }

    private void mockFindAllByUserId(List<Token> tokens) {
        when(tokenRepository.findAllByUserId(userId)).thenReturn(tokens);
    }

    private void mockFindByTokenValue(String tokenValue, Token token) {
        when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(Optional.ofNullable(token));
    }

    private void mockDeleteTokensByUser(User user) {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(tokenRepository.findAllValidTokenByUser(anyString())).thenReturn(Collections.emptyList());
    }

    private User createUser(RegisterRequestDTO registerRequestDTO) {
        return new User(
                registerRequestDTO.name(),
                registerRequestDTO.email(),
                new BCryptPasswordEncoder().encode(registerRequestDTO.password()),
                true,
                UserRole.USER
        );
    }
}