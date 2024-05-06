package org.instituteatri.deep.model.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void testGettersAndSetters() {
        // Given
        User user = new User();
        String name = "Test";
        String email = "Test@example.com";
        String password = "password";
        boolean isEnabled = true;
        UserRole role = UserRole.USER;

        // When
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setEnable(isEnabled);
        user.setRole(role);

        // Then
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(isEnabled, user.isEnable());
        assertEquals(role, user.getRole());
    }
}