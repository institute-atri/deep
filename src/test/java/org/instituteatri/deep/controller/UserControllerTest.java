package org.instituteatri.deep.controller;

import org.instituteatri.deep.dto.request.UpdateUserRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.dto.response.UserResponseDTO;
import org.instituteatri.deep.exception.user.UserNotFoundException;
import org.instituteatri.deep.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    private final String userId = "123";

    @Nested
    @DisplayName("Test Get All Users Method")
    class testGetAllUsersMethod {
        @Test
        @DisplayName("Should get all users with success")
        void shouldGetAllUsersWithSuccess() {
            // Arrange
            List<UserResponseDTO> expectedResponseUserDTO = new ArrayList<>();
            expectedResponseUserDTO.add(new UserResponseDTO(
                    userId,
                    "Name",
                    "test@localhost.com"
            ));
            expectedResponseUserDTO.add(new UserResponseDTO(
                    "1234",
                    "Name",
                    "user@localhost.com"
            ));
            when(userService.getAllUsers()).thenReturn(ResponseEntity.ok(expectedResponseUserDTO));

            // Act
            ResponseEntity<List<UserResponseDTO>> responseEntityUsers = userController.getAllUsers();

            // Assert
            assertThat(responseEntityUsers.getBody()).isEqualTo(expectedResponseUserDTO);
            assertThat(responseEntityUsers.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(userService).getAllUsers();
        }

        @Test
        @DisplayName("Should get all users with success when not found")
        void shouldGetAllUsersWithSuccessWhenNotFound() {
            // Arrange
            when(userService.getAllUsers()).thenThrow(new UserNotFoundException("No users found"));

            // Act
            Exception exception = assertThrows(UserNotFoundException.class, () -> userController.getAllUsers());

            // Assert
            assertThat(exception.getMessage()).isEqualTo("No users found");
        }
    }

    @Nested
    @DisplayName("Tests Get User By Id Method")
    class testGetUserByIdMethod {
        @Test
        @DisplayName("Should get user by id with success")
        void shouldGetUserByIdWithSuccess() {
            // Arrange
            String expectedId = "123";
            UserResponseDTO expectedResponseDTO = new UserResponseDTO(
                    expectedId,
                    "Name",
                    "user@localhost.com"
            );
            when(userService.getByUserId(expectedId)).thenReturn(ResponseEntity.ok(expectedResponseDTO));

            // Act
            ResponseEntity<UserResponseDTO> responseEntityUserId = userController.getByUserId(expectedId);

            // Assert
            assertThat(responseEntityUserId.getBody()).isEqualTo(expectedResponseDTO);
            assertThat(responseEntityUserId.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(userService).getByUserId(expectedId);
        }

        @Test
        @DisplayName("Should get user by id with success when not found")
        void shouldGetUserByIdWithSuccessWhenNotFound() {
            // Arrange
            when(userService.getByUserId(userId)).thenThrow(new UserNotFoundException(userId));

            // Act
            Exception exception = assertThrows(UserNotFoundException.class, () -> userController.getByUserId(userId));

            // Assert
            assertThat(exception.getMessage()).isEqualTo(new UserNotFoundException(userId).getMessage());
            verify(userService).getByUserId(userId);
        }
    }


    @Nested
    @DisplayName("Tests Update User Method")
    class testUpdateUserMethod {

        UpdateUserRequestDTO updateUserRequestDTO = new UpdateUserRequestDTO(
                "Name",
                "test@example.com",
                "Password123+",
                "Password123+"
        );

        @Test
        @DisplayName("Should update user success")
        void shouldUpdateUserSuccess() {
            // Arrange
            ResponseEntity<TokenResponseDTO> expectedTokenResponse =
                    ResponseEntity.ok().body(new TokenResponseDTO(
                            "Token", "RefreshToken"));
            when(userService.updateUser(userId, updateUserRequestDTO, authentication)).thenReturn(expectedTokenResponse);

            // Act
            ResponseEntity<TokenResponseDTO> responseEntityTokenDTO = userController.updateUser(userId, updateUserRequestDTO, authentication);

            // Assert
            assertThat(responseEntityTokenDTO)
                    .isEqualTo(expectedTokenResponse)
                    .extracting(ResponseEntity::getStatusCode)
                    .isEqualTo(HttpStatus.OK);
            verify(userService).updateUser(userId, updateUserRequestDTO, authentication);
        }

        @Test
        @DisplayName("Should return not found when user is not found")
        void shouldReturnNotFoundWhenUserNotFound() {
            // Arrange
            when(userService.updateUser(userId, updateUserRequestDTO, authentication))
                    .thenThrow(new UserNotFoundException(userId));

            // Act
            Exception exception = assertThrows(UserNotFoundException.class,
                    () -> userController.updateUser(userId, updateUserRequestDTO, authentication));

            // Assert
            assertThat(exception.getMessage()).isEqualTo(new UserNotFoundException(userId).getMessage());
            verify(userService).updateUser(userId, updateUserRequestDTO, authentication);
        }
    }

    @Nested
    class deleteUser {
        @Test
        @DisplayName("Should delete user success")
        void shouldDeleteUserSuccess() {
            // Arrange
            ResponseEntity<Void> expectedResponseDelete = ResponseEntity.noContent().build();
            when(userService.deleteUser(userId)).thenReturn(expectedResponseDelete);

            // Act
            ResponseEntity<Void> responseEntityDelete = userController.deleteUser(userId);

            // Assert
            assertThat(responseEntityDelete)
                    .isEqualTo(expectedResponseDelete)
                    .extracting(ResponseEntity::getStatusCode)
                    .isEqualTo(HttpStatus.NO_CONTENT);
            verify(userService).deleteUser(userId);
        }

        @Test
        @DisplayName("Should return not found when user is not found")
        void shouldReturnNotFoundWhenUserNotFound() {
            // Arrange
            when(userService.deleteUser(userId))
                    .thenThrow(new UserNotFoundException(userId));

            // Act
            Exception exception = assertThrows(UserNotFoundException.class,
                    () -> userController.deleteUser(userId));

            // Assert
            assertThat(exception.getMessage()).isEqualTo(new UserNotFoundException(userId).getMessage());
            verify(userService).deleteUser(userId);
        }
    }
}