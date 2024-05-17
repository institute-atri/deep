package org.instituteatri.deep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instituteatri.deep.dto.request.LoginRequestDTO;
import org.instituteatri.deep.dto.request.RefreshTokenRequestDTO;
import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AccountService accountService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void getLogin_WithValidCredentials_ReturnsResponseEntityWithStatus200() throws Exception {
        // Given
        LoginRequestDTO authDto = new LoginRequestDTO("test@localhost.com", "@Test123k+");
        ResponseEntity<TokenResponseDTO> expectedResponse = new ResponseEntity<>(new TokenResponseDTO("token", "refreshToken"), HttpStatus.OK);

        // When
        when(accountService.loginAccount(any(LoginRequestDTO.class), any(AuthenticationManager.class))).thenReturn(expectedResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authDto)))
                .andExpect(status().isOk());
    }

    @Test
    void register_WithValidRegisterDTO_ReturnsResponseEntityWithStatus201() {
        // Given
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "Test User",
                "test@example.com",
                "Password123+",
                "Password123+");

        ResponseEntity<TokenResponseDTO> expectedResponse = new ResponseEntity<>(HttpStatus.CREATED);
        when(accountService.registerAccount(any(RegisterRequestDTO.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<TokenResponseDTO> actualResponse = authenticationController.register(registerRequestDTO);

        // Then
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void logout_WhenCalled_ReturnsResponseEntityWithStatus200() throws Exception {
        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/logout"))
                .andExpect(status().isOk());
    }

    @Test
    void refreshToken_WithValidRefreshToken_ReturnsResponseEntityWithStatus200() {
        // Given
        RefreshTokenRequestDTO tokenDTO = new RefreshTokenRequestDTO("newToken");
        ResponseEntity<TokenResponseDTO> expectedResponse = new ResponseEntity<>(new TokenResponseDTO("newToken", "newRefreshToken"), HttpStatus.OK);
        when(accountService.refreshToken(any(RefreshTokenRequestDTO.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<TokenResponseDTO> responseEntity = authenticationController.refreshToken(tokenDTO);

        // Then
        assertThat(responseEntity)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody())
                .isNotNull()
                .extracting(TokenResponseDTO::token, TokenResponseDTO::refreshToken)
                .containsExactly("newToken", "newRefreshToken");
    }
}
