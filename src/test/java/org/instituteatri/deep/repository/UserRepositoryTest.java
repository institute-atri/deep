package org.instituteatri.deep.repository;

import jakarta.persistence.EntityManager;
import org.instituteatri.deep.model.user.User;
import org.instituteatri.deep.model.user.UserRole;
import org.instituteatri.deep.dto.request.RegisterRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Value("${emailTest}")
    private String emailTest;

    @Value("${passwordTest}")
    private String passwordTest;

    @Test
    @DisplayName("Should get user successfully from database")
    void findByEmailSuccess() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "John",
                emailTest,
                passwordTest,
                passwordTest);
        this.createUser(registerRequestDTO);

        User result = (User) this.userRepository.findByEmail(registerRequestDTO.email());
        assertThat(result.getEmail()).isEqualTo(registerRequestDTO.email());
    }

    @Test
    @DisplayName("Should not retrieve user from the database when user does not exist")
    void findByEmailFail() {
        // Given an email that doesn't exist in the database
        String nonExistentEmail = "nonExistent@me.com";

        // When attempting to find a user with that email
        User result = (User) userRepository.findByEmail(nonExistentEmail);

        // Then the result should be null
        assertThat(result).isNull();
    }


    private void createUser(RegisterRequestDTO registerRequestDTO) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequestDTO.password());
        User newUser = new User(
                registerRequestDTO.name(),
                registerRequestDTO.email(),
                encryptedPassword,
                true,
                UserRole.USER
        );
        this.entityManager.persist(newUser);
    }
}