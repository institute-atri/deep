package org.instituteatri.deep.service.strategy.interfaces;

import org.instituteatri.deep.domain.user.User;
import org.instituteatri.deep.dtos.user.AuthenticationDTO;
import org.instituteatri.deep.dtos.user.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

public interface AccountLoginManager {

    Authentication authenticateUserComponent(AuthenticationDTO authDto, AuthenticationManager authManager);
    void handleSuccessfulLoginComponent(User user);
    ResponseEntity<ResponseDTO> handleBadCredentialsComponent(String email);
}
