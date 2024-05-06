package org.instituteatri.deep.service.strategy.interfaces;

import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.dto.request.LoginRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

public interface AccountLoginManager {

    Authentication authenticateUserComponent(LoginRequestDTO authDto, AuthenticationManager authManager);
    void handleSuccessfulLoginComponent(User user);
    ResponseEntity<TokenResponseDTO> handleBadCredentialsComponent(String email);
}
