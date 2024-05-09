package org.instituteatri.deep.dto.request;

public record RegisterRequestDTO(
        String name,
        String email,
        String password,
        String confirmPassword) {
}
