package org.instituteatri.deep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instituteatri.deep.dtos.user.AuthenticationDTO;
import org.instituteatri.deep.dtos.user.RefreshTokenDTO;
import org.instituteatri.deep.dtos.user.RegisterDTO;
import org.instituteatri.deep.dtos.user.ResponseDTO;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        AuthenticationDTO authDto = new AuthenticationDTO("username", "password");
        ResponseEntity<ResponseDTO> expectedResponse = new ResponseEntity<>(new ResponseDTO("token", "refreshToken"), HttpStatus.OK);

        // When
        when(accountService.loginAccount(any(AuthenticationDTO.class), any(AuthenticationManager.class))).thenReturn(expectedResponse);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authDto)))
                .andExpect(status().isOk());
    }

    @Test
    void register_WithValidRegisterDTO_ReturnsResponseEntityWithStatus201() {
        // Given
        RegisterDTO registerDTO = new RegisterDTO(
                "Test User",
                "test@example.com",
                "Password123+",
                "Password123+");

        ResponseEntity<ResponseDTO> expectedResponse = new ResponseEntity<>(HttpStatus.CREATED);
        when(accountService.registerAccount(any(RegisterDTO.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ResponseDTO> actualResponse = authenticationController.register(registerDTO);

        // Then
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
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
        RefreshTokenDTO tokenDTO = new RefreshTokenDTO("newToken");
        ResponseEntity<ResponseDTO> expectedResponse = new ResponseEntity<>(new ResponseDTO("newToken", "newRefreshToken"), HttpStatus.OK);
        when(accountService.refreshToken(any(RefreshTokenDTO.class))).thenReturn(expectedResponse);

        // When
        ResponseEntity<ResponseDTO> responseEntity = authenticationController.refreshToken(tokenDTO);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("newToken", Objects.requireNonNull(responseEntity.getBody()).token());
        assertEquals("newRefreshToken", responseEntity.getBody().refreshToken());
    }
}
