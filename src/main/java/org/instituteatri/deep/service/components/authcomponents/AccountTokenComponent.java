package org.instituteatri.deep.service.components.authcomponents;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.domain.token.Token;
import org.instituteatri.deep.domain.token.TokenType;
import org.instituteatri.deep.domain.user.User;
import org.instituteatri.deep.dtos.user.ResponseDTO;
import org.instituteatri.deep.infrastructure.security.TokenService;
import org.instituteatri.deep.repositories.TokenRepository;
import org.instituteatri.deep.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountTokenComponent {

    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public ResponseDTO generateTokenResponse(User user) {
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        clearTokens(user.getId());
        Token accessTokenToken = saveUserToken(user, accessToken);
        Token refreshTokenToken = saveUserToken(user, refreshToken);

        user.getTokens().clear();
        user.getTokens().add(accessTokenToken);
        user.getTokens().add(refreshTokenToken);
        userRepository.save(user);

        return new ResponseDTO(accessTokenToken.getTokenValue(), refreshTokenToken.getTokenValue());
    }

    private void clearTokens(String userId) {
        var tokens = tokenRepository.findAllByUserId(userId);
        if (!tokens.isEmpty()) {
            tokenRepository.deleteAll(tokens);
        }
    }

    private Token saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .tokenValue(jwtToken)
                .tokenType(TokenType.BEARER)
                .tokenExpired(false)
                .tokenRevoked(false)
                .build();
        return tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setTokenExpired(true);
                token.setTokenRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }
}
