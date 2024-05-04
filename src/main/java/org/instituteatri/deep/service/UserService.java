package org.instituteatri.deep.service;

import lombok.RequiredArgsConstructor;
import org.instituteatri.deep.domain.user.User;
import org.instituteatri.deep.dtos.user.RegisterDTO;
import org.instituteatri.deep.dtos.user.UserDTO;
import org.instituteatri.deep.infrastructure.exceptions.user.*;
import org.instituteatri.deep.mappings.UserMapper;
import org.instituteatri.deep.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();

        return ResponseEntity.ok(users.stream()
                .map(userMapper::toUserDto)
                .toList());
    }

    public UserDTO getByUserId(String id) {
        Optional<User> user = userRepository.findById(id);

        return user.map(userMapper::toUserDto).orElseThrow(() -> new UserNotFoundException(id));
    }

    public ResponseEntity<Void> deleteUser(String id) {
        User existingUser = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));

        userRepository.delete(existingUser);

        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> updateUser(String id, RegisterDTO user, Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }

        if (!user.password().equals(user.confirmPassword())) {
            throw new PasswordsNotMatchException();
        }

        String authenticatedUserId = ((User) authentication.getPrincipal()).getId();

        if (!id.equals(authenticatedUserId)) {
            throw new UserAccessDeniedException();
        }

        performUserUpdate(id, user);

        return ResponseEntity.noContent().build();
    }

    private void performUserUpdate(String userId, RegisterDTO updatedUserDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        validateEmailUpdate(existingUser, updatedUserDto);

        updateUserProperties(existingUser, updatedUserDto);

        userRepository.save(existingUser);
    }

    private void updateUserProperties(User existingUser, RegisterDTO updatedUserDto) {
        updateField(existingUser::setName, existingUser.getName(), updatedUserDto.name());
        updateField(existingUser::setEmail, existingUser.getEmail(), updatedUserDto.email());
        updatePassword(existingUser, updatedUserDto.password());

        if (updatedUserDto.password() != null && !updatedUserDto.password().isEmpty()) {
            updatePassword(existingUser, updatedUserDto.password());
        }
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

    private void validateEmailUpdate(User existingUser, RegisterDTO updatedUserDto) {
        String newEmail = updatedUserDto.email();

        if (!existingUser.getEmail().equals(newEmail) &&
                checkIfEmailExists(newEmail, String.valueOf(existingUser.getId()))) {
            throw new EmailAlreadyExistsException();
        }
    }

    private boolean checkIfEmailExists(String email, String userIdToExclude) {
        User user = (User) userRepository.findByEmail(email);
        return user != null && !user.getId().equals(userIdToExclude);
    }
}