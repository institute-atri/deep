package org.instituteatri.deep.model.user;

import lombok.Getter;

@Getter
/*
 * TODO: This enum is still under development.
 * Additional implementations will be added as needed.
 */
public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
