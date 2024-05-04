package org.instituteatri.deep.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dtos.user.AuthenticationDTO;
import org.instituteatri.deep.dtos.user.RefreshTokenDTO;
import org.instituteatri.deep.dtos.user.RegisterDTO;
import org.instituteatri.deep.dtos.user.ResponseDTO;
import org.instituteatri.deep.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> getLogin(@RequestBody AuthenticationDTO authDto) {
        return accountService.loginAccount(authDto, authenticationManager);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody @Valid RegisterDTO registerDTO) {
        return accountService.registerAccount(registerDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDTO> refreshToken(@RequestBody RefreshTokenDTO tokenDTO) {
        return accountService.refreshToken(tokenDTO);
    }
}
