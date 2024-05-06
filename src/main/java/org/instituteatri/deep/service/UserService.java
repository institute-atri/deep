package org.instituteatri.deep.service;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.instituteatri.deep.dto.response.TokenResponseDTO;
import org.instituteatri.deep.dto.response.UserResponseDTO;
import org.instituteatri.deep.exception.user.UserNotFoundException;
import org.instituteatri.deep.mapper.UserMapper;
import org.instituteatri.deep.repository.TokenRepository;
import org.instituteatri.deep.repository.UserRepository;
import org.instituteatri.deep.service.strategy.interfaces.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationTokenManager authTokenManager;
    private final AuthenticationValidationStrategy authValidationStrategy;
    private final PasswordValidationStrategy passwordValidationStrategy;
    private final UserIdValidationStrategy userIdValidationStrategy;
    private final EmailAlreadyValidationStrategy emailAlreadyValidationStrategy;

    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(users.stream()
                .map(userMapper::toUserDto)
                .toList());
    }

    public UserResponseDTO getByUserId(String id) {
        Optional<User> user = userRepository.findById(id);

        return user.map(userMapper::toUserDto).orElseThrow(()
                -> new UserNotFoundException(id));
    }

    @Transactional
    public ResponseEntity<Void> deleteUser(String userId) {

        User existingUser = findUserByIdOrThrow(userId);

        tokenRepository.deleteByUser(existingUser);

        userRepository.delete(existingUser);

        return ResponseEntity.noContent().build();
    }


    /**
     * When a user makes changes to their data,
     * such as updating their name or email,
     * it triggers the need to update the authentication token associated with their account.
     * This is because the authentication token serves as a representation of the user's details,
     * and any changes to these details necessitate an update to the token to reflect the new information.
     * To ensure the correct update of the authentication token, the following steps are taken:
     * 1. Revoking all old tokens associated with the user's account using the revokeAllUserTokens method.
     * 2. Generating a new updated authentication token using the generateTokenResponse method.
     * This method returns both the access token and the refresh token,
     * ensuring that the user maintains access to the system with their updated information
     * while preserving account security.
     *
     * @param userId         The ID of the user whose information is being updated.
     * @param updatedUserDto The updated user data.
     * @param authentication The authentication details of the user.
     * @return A ResponseEntity containing the ResponseDTO with the access and refresh tokens.
     */
    @Transactional
    public ResponseEntity<TokenResponseDTO> updateUser(String userId, RegisterRequestDTO updatedUserDto, Authentication authentication) {

        User existingUser = findUserByIdOrThrow(userId);

        authValidationStrategy.validate(authentication);

        passwordValidationStrategy.validate(updatedUserDto.password(), updatedUserDto.confirmPassword());

        userIdValidationStrategy.validate(authentication, userId);

        validateEmailUpdate(existingUser, updatedUserDto);
        updateUserProperties(existingUser, updatedUserDto);
        userRepository.save(existingUser);

        authTokenManager.revokeAllUserTokens(existingUser);

        return ResponseEntity.ok(authTokenManager.generateTokenResponse(existingUser));
    }

    private User findUserByIdOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void updateUserProperties(User existingUser, RegisterRequestDTO updatedUserDto) {
        updateField(existingUser::setName, existingUser.getName(), updatedUserDto.name());
        updateField(existingUser::setEmail, existingUser.getEmail(), updatedUserDto.email());
        updatePassword(existingUser, updatedUserDto.password());
    }

    private void updatePassword(User existingUser, String newPassword) {
        if (newPassword != null && !passwordEncoder.matches(newPassword, existingUser.getPassword())) {
            String encryptedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encryptedPassword);
        }
    }

    private <T> void updateField(Consumer<T> setter, T currentValue, T newValue) {
        if (newValue != null && !newValue.equals(currentValue)) {
            setter.accept(newValue);
        }
    }

    private void validateEmailUpdate(User existingUser, RegisterRequestDTO updatedUserDto) {
        String newEmail = updatedUserDto.email();

        emailAlreadyValidationStrategy.validate(
                existingUser.getEmail(),
                newEmail,
                existingUser.getId()
        );
    }
}