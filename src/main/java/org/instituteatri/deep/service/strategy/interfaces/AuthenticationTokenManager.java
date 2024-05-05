package org.instituteatri.deep.service.strategy.interfaces;

import org.instituteatri.deep.domain.user.User;
import org.instituteatri.deep.dtos.user.ResponseDTO;

public interface AuthenticationTokenManager {
    ResponseDTO generateTokenResponse(User user);
    void revokeAllUserTokens(User user);
}
