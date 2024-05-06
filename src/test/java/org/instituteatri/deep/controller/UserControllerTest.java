package org.instituteatri.deep.controller;

import org.instituteatri.deep.dtos.user.RegisterDTO;
import org.instituteatri.deep.dtos.user.ResponseDTO;
import org.instituteatri.deep.dtos.user.UserDTO;
import org.instituteatri.deep.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final RegisterDTO registerDTO = new RegisterDTO(
            "Test User",
            "test@example.com",
            "Password123+",
            "Password123+"
    );
    private final String id = "123";
    private final Authentication authentication = Mockito.mock(Authentication.class);

    @Test
    void getAllUsers_WhenServiceReturnsMultipleUsers_ReturnsListOfUsers() {
        // Given
        List<UserDTO> expectedUsers = Arrays.asList(
                new UserDTO("testId1", "testName1", "testEmail1@example.com"),
                new UserDTO("testId2", "testName2", "testEmail2@example.com")
        );
        ResponseEntity<List<UserDTO>> expectedResponse = ResponseEntity.ok(expectedUsers);
        when(userService.getAllUsers()).thenReturn(expectedResponse);

        // When
        ResponseEntity<List<UserDTO>> result = userController.getAllUsers();

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result);
        assertEquals(expectedUsers, result.getBody());
    }

    @Test
    void testGetAllUsers_WhenServiceReturnsEmptyList_ShouldReturnEmptyList() {
        // Given
        List<UserDTO> expectedUsers = Collections.emptyList();
        ResponseEntity<List<UserDTO>> expectedResponse = ResponseEntity.ok(expectedUsers);
        when(userService.getAllUsers()).thenReturn(expectedResponse);

        // When
        ResponseEntity<List<UserDTO>> result = userController.getAllUsers();

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result);
        assertEquals(expectedUsers, result.getBody());
    }


    @Test
    void testGetUserById_WhenValidId_ThenReturnUser() {
        // Given
        String userId = "testId1";
        UserDTO expectedUser = new UserDTO(userId, "testName1", "testEmail1@example.com");
        when(userService.getByUserId(userId)).thenReturn(expectedUser);

        // When
        ResponseEntity<UserDTO> response = userController.getByUserId(userId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
    }


    @Test
    void updateUser_WhenValidRequest_ThenReturnUpdatedUser() {
        // Given
        ResponseEntity<ResponseDTO> expectedResponse = ResponseEntity.ok().body(new ResponseDTO("Token", "RefreshToken"));
        when(userService.updateUser(id, registerDTO, authentication)).thenReturn(expectedResponse);

        // When
        ResponseEntity<ResponseDTO> responseEntity = userController.updateUser(id, registerDTO, authentication);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity);
    }

    @Test
    void deleteUser_WhenValidRequest_ThenReturnNoContentResponse() {
        // Given
        ResponseEntity<Void> expectedResponse = ResponseEntity.noContent().build();
        when(userService.deleteUser(id)).thenReturn(expectedResponse);

        // When
        ResponseEntity<Void> responseEntity = userController.deleteUser(id);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity);
    }
}