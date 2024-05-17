package org.instituteatri.deep.service.strategy.interfaces;

import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.dto.response.TokenResponseDTO;

public interface AuthenticationTokenManager {
    TokenResponseDTO generateTokenResponse(User user);
    void revokeAllUserTokens(User user);
}
