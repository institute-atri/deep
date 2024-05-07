package org.instituteatri.deep.controller;

import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.dto.response.UserResponseDTO;
import org.instituteatri.deep.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
            "Test User",
            "test@example.com",
            "Password123+",
            "Password123+"
    );
    private final String id = "123";
    private final Authentication authentication = Mockito.mock(Authentication.class);

    @Test
    @DisplayName("When the service returns multiple users, it should return a list of users")
    void getAllUsers_WhenServiceReturnsMultipleUsers_ReturnsListOfUsers() {
        // Given
        List<UserResponseDTO> expectedUsers = Arrays.asList(
                new UserResponseDTO("testId1", "testName1", "testEmail1@example.com"),
                new UserResponseDTO("testId2", "testName2", "testEmail2@example.com")
        );
        ResponseEntity<List<UserResponseDTO>> expectedResponse = ResponseEntity.ok(expectedUsers);
        when(userService.getAllUsers()).thenReturn(expectedResponse);

        // When
        ResponseEntity<List<UserResponseDTO>> responseEntity = userController.getAllUsers();

        // Then
        assertThat(responseEntity)
                .isEqualTo(expectedResponse)
                .extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
                .containsExactly(HttpStatus.OK, expectedUsers);
    }

    @Test
    @DisplayName("When the service returns an empty list, it must return an empty list")
    void testGetAllUsers_WhenServiceReturnsEmptyList_ShouldReturnEmptyList() {
        // Given
        List<UserResponseDTO> expectedUsers = Collections.emptyList();
        ResponseEntity<List<UserResponseDTO>> expectedResponse = ResponseEntity.ok(expectedUsers);
        when(userService.getAllUsers()).thenReturn(expectedResponse);

        // When
        ResponseEntity<List<UserResponseDTO>> responseEntity = userController.getAllUsers();

        // Then
        assertThat(responseEntity)
                .isEqualTo(expectedResponse)
                .extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
                .containsExactly(HttpStatus.OK, expectedUsers);
    }


    @Test
    @DisplayName("When the ID is valid, it should return the corresponding user")
    void testGetUserById_WhenValidId_ThenReturnUser() {
        // Given
        String userId = "testId1";
        UserResponseDTO expectedResponse = new UserResponseDTO(userId, "testName1", "testEmail1@example.com");
        when(userService.getByUserId(userId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<UserResponseDTO> responseEntity = userController.getByUserId(userId);

        // Then
        assertThat(responseEntity.getBody())
                .isEqualTo(expectedResponse);
        assertThat(responseEntity.getStatusCode())
                .isEqualTo(HttpStatus.OK);
    }


    @Test
    @DisplayName("Upon receiving a valid request, it should return the updated user")
    void updateUser_WhenValidRequest_ThenReturnUpdatedUser() {
        // Given
        ResponseEntity<TokenResponseDTO> expectedResponse = ResponseEntity.ok().body(new TokenResponseDTO("Token", "RefreshToken"));
        when(userService.updateUser(id, registerRequestDTO, authentication)).thenReturn(expectedResponse);

        // When
        ResponseEntity<TokenResponseDTO> responseEntity = userController.updateUser(id, registerRequestDTO, authentication);

        // Then
        assertThat(responseEntity)
                .isEqualTo(expectedResponse)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Upon receiving a valid request, it should return an empty content response")
    void deleteUser_WhenValidRequest_ThenReturnNoContentResponse() {
        // Given
        ResponseEntity<Void> expectedResponse = ResponseEntity.noContent().build();
        when(userService.deleteUser(id)).thenReturn(expectedResponse);

        // When
        ResponseEntity<Void> responseEntity = userController.deleteUser(id);

        // Then
        assertThat(responseEntity)
                .isEqualTo(expectedResponse)
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.NO_CONTENT);
    }
}