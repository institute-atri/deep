package org.instituteatri.deep.service;

import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.dto.response.UserResponseDTO;
import org.instituteatri.deep.exception.user.UserNotFoundException;
import org.instituteatri.deep.mapper.UserMapper;
import org.instituteatri.deep.repository.TokenRepository;
import org.instituteatri.deep.repository.UserRepository;
import org.instituteatri.deep.service.strategy.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserMapper userMapper;
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
    private Authentication authentication;

    @Value("${emailTest}")
    private String emailTest;
    private final String userId = "testId";
    private final String userName = "testName";

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {

        // Call mockUserAndDTO to set up the mock objects
        mockUserAndDTO();

        List<User> users = Arrays.asList(new User(), new User());

        // Act
        when(userRepository.findAll()).thenReturn(users);
        ResponseEntity<List<UserResponseDTO>> response = userService.getAllUsers();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        verify(userRepository, times(1)).findAll();

        // Verify that the interaction with userMapper occurred for each user in the list
        verify(userMapper, times(2)).toUserDto(any(User.class));
    }

    @Test
    void getByUserId_UserExists_ReturnsUserDTO() {
        // Arrange
        User mockUser = new User();
        UserResponseDTO mockUserResponseDTO = new UserResponseDTO(
                userId,
                userName,
                emailTest);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userMapper.toUserDto(mockUser)).thenReturn(mockUserResponseDTO);

        // Act
        UserResponseDTO result = userService.getByUserId(userId);

        // Assert
        assertEquals(mockUserResponseDTO, result);
    }

    @Test
    void getByUserId_UserDoesNotExist_ThrowsException() {
        // Arrange
        String userId = "nonExistentId";
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.getByUserId(userId));
    }

    @Test
    void deleteUser_ShouldDeleteUserAndTokens_WhenUserExists() {

        User user = new User();

        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<Void> response = userService.deleteUser(userId);

        // Assert
        verify(userRepository).delete(user);
        verify(tokenRepository).deleteByUser(user);
        assertEquals(ResponseEntity.noContent().build(), response);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository, never()).delete(any(User.class));
        verify(tokenRepository, never()).deleteByUser(any(User.class));
    }

    @Test
    void updateUser_Success() {
        User existingUser = new User();
        existingUser.setId(userId);
        RegisterRequestDTO updatedUserDto = new RegisterRequestDTO(
                "newName",
                "newEmail@example.com",
                "@newPassword1+",
                "@newPassword1+");

        TokenResponseDTO expectedResponse = new TokenResponseDTO("Token", "refreshToken");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(existingUser));
        when(authTokenManager.generateTokenResponse(existingUser)).thenReturn(expectedResponse);

        ResponseEntity<TokenResponseDTO> response = userService.updateUser(userId, updatedUserDto, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(userRepository).save(any(User.class));
        verify(authTokenManager).revokeAllUserTokens(any(User.class));
    }

    private void mockUserAndDTO() {
        User user = new User();
        user.setId(userId);

        UserResponseDTO userResponseDTO = new UserResponseDTO(userId, userName, emailTest);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userResponseDTO);
    }
}