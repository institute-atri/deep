package org.instituteatri.deep.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.dto.request.LoginRequestDTO;
import org.instituteatri.deep.dto.request.RefreshTokenRequestDTO;
import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Endpoints for user authentication management")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    @Operation(
            method = "POST",
            summary = "Authenticate user by verifying credentials.",
            description = "Endpoint for authentication. " +
                    "If a user enters the wrong password more than four times," +
                    " their account will be locked. Upon successful login, " +
                    "returns a token and refresh-token.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"token\": [\"string\"]," +
                                            "\"refresh-token\": [\"string\"]" +
                                            "}"
                            ))),

            @ApiResponse(responseCode = "404", description = "Not found.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Invalid username or password.\"}"
                            ))),

            @ApiResponse(responseCode = "403", description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Account is locked.\"}"
                            ))),
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> getLogin(@RequestBody LoginRequestDTO authDto) {
        return accountService.loginAccount(authDto, authenticationManager);
    }

    @Operation(
            method = "POST",
            summary = "Register a new user.",
            description = "Registers a new user in the system." +
                    " The user information should be provided in the request body." +
                    " Upon successful registration, the endpoint returns a token and a refresh token," +
                    " which can be used for authentication and authorization in subsequent requests.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"token\": [\"string\"]," +
                                            "\"refresh-token\": [\"string\"]" +
                                            "}"
                            ))),

            @ApiResponse(responseCode = "409", description = "Conflicts.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"E-mail not available.\"}"
                            )))
    })
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        return accountService.registerAccount(registerRequestDTO);
    }


    @Operation(
            method = "POST",
            summary = "Logout the current user.",
            description = "Endpoint to logout the currently authenticated user. " +
                    "Invalidates the current session and clears the security context.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "401", description = "Unauthorized.")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        new SecurityContextLogoutHandler().logout(request, response, authentication);
        return ResponseEntity.ok().build();
    }

    @Operation(
            method = "POST",
            summary = "Refresh the access token using a refresh token.",
            description = "Endpoint to refresh the access token using a valid refresh token.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"token\": [\"string\"]," +
                                            "\"refresh-token\": [\"string\"]" +
                                            "}"
                            ))),

            @ApiResponse(responseCode = "404", description = "Not found.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User email not found in token.\"}"
                            ))),

            @ApiResponse(responseCode = "403", description = "Forbidden.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"User unauthorized.\"}"
                            )))
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO tokenDTO) {
        return accountService.refreshToken(tokenDTO);
    }

    @Operation(
            method = "GET",
            summary = "Retrieve CSRF token.",
            description = "Retrieves the Cross-Site Request Forgery (CSRF) token for the current session." +
                    " The CSRF token is a security mechanism used to prevent" +
                    " CSRF attacks by ensuring that requests originate from the same site.")

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CsrfToken.class)
                    ))
    })
    @GetMapping("/csrf-token")
    public CsrfToken csrf(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
}
