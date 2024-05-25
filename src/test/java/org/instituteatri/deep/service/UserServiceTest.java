package org.instituteatri.deep.service;

import org.instituteatri.deep.dto.request.UpdateUserRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.dto.response.UserResponseDTO;
import org.instituteatri.deep.exception.user.EmailAlreadyExistsException;
import org.instituteatri.deep.exception.user.NotAuthenticatedException;
import org.instituteatri.deep.exception.user.PasswordsNotMatchException;
import org.instituteatri.deep.exception.user.UserNotFoundException;
import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.repository.TokenRepository;
import org.instituteatri.deep.repository.UserRepository;
import org.instituteatri.deep.service.strategy.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationTokenManager authTokenManager;

    @Mock
    private AuthenticationValidationStrategy authValidationStrategy;

    @Mock
    private PasswordValidationStrategy passwordValidationStrategy;

    @Mock
    private UserIdValidationStrategy userIdValidationStrategy;

    @Mock
    private EmailAlreadyValidationStrategy emailAlreadyValidationStrategy;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Authentication authentication;

    @Mock
    private UpdateUserRequestDTO updatedUserDto;

    @InjectMocks
    private UserService userService;

    private User existingUser;

    private final String userId = "123";
    String name = "Name";
    String newName = "newName";
    String email = "test@example.com";
    String newPassword = "newPassword";

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setId(userId);
        modelMapper = new ModelMapper();
        userService = new UserService(tokenRepository, userRepository, passwordEncoder, authTokenManager, authValidationStrategy, passwordValidationStrategy, userIdValidationStrategy, emailAlreadyValidationStrategy, modelMapper);
    }

    @Nested
    @DisplayName("Tests Get All Users Method")
    class testGetAllUsersMethod {

        @Test
        @DisplayName("Should Return List of Users When Users Exist")
        void shouldReturnListOfUsersWhenUsersExist() {
            // Arrange
            User user1 = new User();
            user1.setId("1");
            user1.setName("Name 1");

            User user2 = new User();
            user2.setId("2");
            user2.setName("Name 2");

            when(userRepository.findAll()).thenReturn(List.of(user1, user2));

            // Act
            ResponseEntity<List<UserResponseDTO>> responseEntity = userService.getAllUsers();

            // Assert
            assertNotNull(responseEntity);
            assertEquals(2, Objects.requireNonNull(responseEntity.getBody()).size());
            assertEquals("Name 1", responseEntity.getBody().getFirst().getName());
            assertEquals("Name 2", responseEntity.getBody().get(1).getName());
        }

        @Test
        @DisplayName("Should Throw UserNotFoundException When No Users Found")
        void shouldThrowUserNotFoundExceptionWhenNoUsersFound() {
            // Arrange
            when(userRepository.findAll()).thenReturn(new ArrayList<>());

            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> userService.getAllUsers());
        }
    }

    @Nested
    @DisplayName("Tests Get User By Id Method")
    class testGetUserByIdMethod {

        @Test
        @DisplayName("Should Return UserResponseDTO When User Exists")
        void shouldReturnUserResponseDTOWhenUserExists() {
            // Arrange
            existingUser.setName(name);

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // Act
            ResponseEntity<UserResponseDTO> responseUserDTO = userService.getByUserId(userId);

            // Assert
            assertNotNull(responseUserDTO);
            assertThat(responseUserDTO.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertEquals(userId, Objects.requireNonNull(responseUserDTO.getBody()).getId());
            assertEquals(name, responseUserDTO.getBody().getName());
        }

        @Test
        @DisplayName("Should Throw UserNotFoundException When User Does Not Exist")
        void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
            // Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> userService.getByUserId(userId));
        }

        @Test
        @DisplayName("Should Throw UserNotFoundException When User ID Is Null")
        void shouldThrowUserNotFoundExceptionWhenUserIdIsNull() {
            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> userService.getByUserId(null));
        }

        @Test
        @DisplayName("Should Throw UserNotFoundException When User ID Is Null")
        void shouldThrowUserNotFoundExceptionWhenUserIdIsEmpty() {
            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> userService.getByUserId(""));
        }

        @Test
        @DisplayName("Should Throw UserNotFoundException When User ID Has Invalid Format")
        void shouldThrowUserNotFoundExceptionWhenUserIdHasInvalidFormat() {
            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> userService.getByUserId("invalid-id-format"));
        }
    }

    @Nested
    @DisplayName("Tests Delete User Method")
    class testDeleteUserMethod {

        @Test
        @DisplayName("Should Delete User and Tokens When User Exists")
        void shouldDeleteUserAndTokens_WhenUserExists() {
            // Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // Act
            ResponseEntity<Void> response = userService.deleteUser(userId);

            // Assert
            assertEquals(ResponseEntity.noContent().build(), response);
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).delete(existingUser);
            verify(tokenRepository).deleteByUser(existingUser);
        }

        @Test
        @DisplayName("Should Throw Exception When User Does Not Exist")
        void shouldThrowException_WhenUserDoesNotExist() {
            // Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, never()).delete(any(User.class));
            verify(tokenRepository, never()).deleteByUser(any(User.class));
        }
    }

    @Nested
    @DisplayName("Tests Update User Method")
    class testUpdateUserMethod {

        UpdateUserRequestDTO updateUserRequestDTO = new UpdateUserRequestDTO(
                newName,
                "new@example.com",
                "newPassword123",
                "newPassword123");

        @Test
        @DisplayName("Should Throw UserNotFoundException When User Is Not Found During Update")
        void shouldThrowUserNotFoundExceptionWhenUserIsNotFoundDuringUpdate() {
            // Arrange
            when(userRepository.findById("nonexistentUserId")).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UserNotFoundException.class,
                    () -> userService.updateUser("nonexistentUserId", updateUserRequestDTO, authentication));
        }

        @Test
        @DisplayName("Should Successfully Update User and Return Token Response")
        void shouldSuccessfullyUpdateUserAndReturnTokenResponse() {
            // Arrange
            existingUser.setEmail("oldEmail@example.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
            when(authTokenManager.generateTokenResponse(any())).thenReturn(
                    new TokenResponseDTO("token", "refreshToken"));

            // Act
            ResponseEntity<TokenResponseDTO> responseEntitySuccess = userService.updateUser(userId, updateUserRequestDTO, authentication);

            // Assert
            assertNotNull(responseEntitySuccess);
            assertEquals("token", Objects.requireNonNull(responseEntitySuccess.getBody()).token());
            assertEquals("refreshToken", Objects.requireNonNull(responseEntitySuccess.getBody()).refreshToken());
            verify(userRepository).save(existingUser);
            verify(authTokenManager).revokeAllUserTokens(existingUser);
        }

        @Test
        @DisplayName("Should Throw PasswordsNotMatchException When Passwords Do Not Match During User Update")
        void shouldThrowPasswordsNotMatchExceptionWhenPasswordsDoNotMatchDuringUserUpdate() {
            // Arrange
            String confirmPassword = "differentPassword";
            UpdateUserRequestDTO updateRequestDTO = new UpdateUserRequestDTO(
                    newName,
                    "newEmail@localhost.com",
                    newPassword,
                    confirmPassword);

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            doThrow(new PasswordsNotMatchException()).when(passwordValidationStrategy).validate(newPassword, confirmPassword);

            // Act & Assert
            assertThrows(PasswordsNotMatchException.class, () ->
                    userService.updateUser(userId, updateRequestDTO, authentication)
            );

            verify(userRepository, times(1)).findById(userId);
            verify(passwordValidationStrategy, times(1)).validate(newPassword, confirmPassword);
            verify(authValidationStrategy, times(1)).validate(authentication);
            verify(userIdValidationStrategy, never()).validate(authentication, userId);
            verify(userRepository, never()).save(any(User.class));
            verify(authTokenManager, never()).revokeAllUserTokens(existingUser);
            verify(authTokenManager, never()).generateTokenResponse(existingUser);
        }

        @Test
        @DisplayName("Should Throw NotAuthenticatedException When User is Not Authenticated During Update")
        void shouldThrowNotAuthenticatedExceptionWhenUserIsNotAuthenticatedDuringUpdate() {
            // Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            doThrow(new NotAuthenticatedException())
                    .when(authValidationStrategy)
                    .validate(authentication);

            // Act & Assert
            assertThrows(NotAuthenticatedException.class, () -> userService.updateUser(userId, updateUserRequestDTO, authentication));
        }

        @Test
        @DisplayName("Should Throw Exception When Email Already Exists During User Update")
        void shouldThrowExceptionWhenEmailAlreadyExistsDuringUserUpdate() {
            // Arrange
            existingUser.setEmail("existing@example.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            doNothing().when(authValidationStrategy).validate(authentication);
            doNothing().when(passwordValidationStrategy).validate(updateUserRequestDTO.password(), updateUserRequestDTO.confirmPassword());
            doNothing().when(userIdValidationStrategy).validate(authentication, userId);

            doThrow(new EmailAlreadyExistsException()).when(userRepository).save(any(User.class));

            // Act & Assert
            assertThrows(EmailAlreadyExistsException.class, () ->
                    userService.updateUser(userId, updateUserRequestDTO, authentication));

            verify(userRepository, times(1)).findById(userId);
            verify(authValidationStrategy, times(1)).validate(authentication);
            verify(passwordValidationStrategy, times(1)).validate(updateUserRequestDTO.password(), updateUserRequestDTO.confirmPassword());
            verify(userIdValidationStrategy, times(1)).validate(authentication, userId);
            verify(userRepository, times(1)).save(any(User.class));
            verify(authTokenManager, never()).revokeAllUserTokens(existingUser);
            verify(authTokenManager, never()).generateTokenResponse(existingUser);
        }
    }

    @Nested
    @DisplayName("Tests Update Field Method")
    class testUpdateFieldMethod {

        @Test
        @DisplayName("Should update the field when the new value is different")
        void shouldUpdateField_WhenNewValueIsDifferent() {
            // Arrange
            existingUser.setName(name);

            // Act
            userService.updateField(existingUser::setName, existingUser.getName(), "NameDif");

            // Assert
            assertEquals("NameDif", existingUser.getName());
        }

        @Test
        @DisplayName("Should not update the field when the new value is null")
        void shouldNotUpdateField_WhenNewValueIsNull() {
            // Arrange
            existingUser.setName(name);

            // Act
            userService.updateField(existingUser::setName, existingUser.getName(), null);

            // Assert
            assertEquals(name, existingUser.getName());
        }

        @Test
        @DisplayName("Should not update the field when the new value is the same as the current value")
        void shouldNotUpdateField_WhenNewValueIsSameAsCurrentValue() {
            // Arrange
            existingUser.setName("SameName");

            // Act
            userService.updateField(existingUser::setName, existingUser.getName(), "SameName");

            // Assert
            assertEquals("SameName", existingUser.getName());
        }

        @Test
        @DisplayName("Should update the field when the current value is null")
        void shouldUpdateField_WhenCurrentValueIsNull() {
            // Act
            userService.updateField(existingUser::setName, null, newName);

            // Assert
            assertEquals(newName, existingUser.getName());
        }
    }

    @Nested
    @DisplayName("Tests Update User Properties Method")
    class UpdateUserPropertiesMethod {

        String oldPasswordHash = "oldPasswordHash";

        @Test
        @DisplayName("Should update all fields when new values are different")
        void shouldUpdateAllFields() {
            // Arrange
            when(updatedUserDto.name()).thenReturn(newName);
            when(updatedUserDto.email()).thenReturn(email);
            when(updatedUserDto.password()).thenReturn(newPassword);

            existingUser.setName("Old Name");
            existingUser.setEmail("oldEmail@example.com");
            existingUser.setPassword(oldPasswordHash);

            when(passwordEncoder.matches(newPassword, oldPasswordHash)).thenReturn(false);
            when(passwordEncoder.encode(newPassword)).thenReturn("newPasswordHash");

            // Act
            userService.updateUserProperties(existingUser, updatedUserDto);

            // Assert
            assertEquals(newName, existingUser.getName());
            assertEquals(email, existingUser.getEmail());
            assertEquals("newPasswordHash", existingUser.getPassword());
        }

        @Test
        @DisplayName("Should not update fields when new values are the same as current values")
        void shouldNotUpdateFields_WhenNewValuesAreSame() {
            // Arrange
            when(updatedUserDto.name()).thenReturn(name);
            when(updatedUserDto.email()).thenReturn(email);
            when(updatedUserDto.password()).thenReturn("oldPassword");

            existingUser.setName(name);
            existingUser.setEmail(email);
            existingUser.setPassword(oldPasswordHash);

            when(passwordEncoder.matches("oldPassword", oldPasswordHash)).thenReturn(true);

            // Act
            userService.updateUserProperties(existingUser, updatedUserDto);

            // Assert
            assertEquals(name, existingUser.getName());
            assertEquals(email, existingUser.getEmail());
            assertEquals(oldPasswordHash, existingUser.getPassword());
        }

        @Test
        @DisplayName("Should not update fields when new values are null")
        void shouldNotUpdateFields_WhenNewValuesAreNull() {
            // Arrange
            when(updatedUserDto.name()).thenReturn(null);
            when(updatedUserDto.email()).thenReturn(null);
            when(updatedUserDto.password()).thenReturn(null);

            existingUser.setName(name);
            existingUser.setEmail(email);
            existingUser.setPassword(oldPasswordHash);

            // Act
            userService.updateUserProperties(existingUser, updatedUserDto);

            // Assert
            assertEquals(name, existingUser.getName());
            assertEquals(email, existingUser.getEmail());
            assertEquals(oldPasswordHash, existingUser.getPassword());
        }
    }
}