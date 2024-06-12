package org.instituteatri.deep.dto.request;

public record UpdateUserRequestDTO(
        String name,
        String email,
        String password,
        String confirmPassword) {
}
