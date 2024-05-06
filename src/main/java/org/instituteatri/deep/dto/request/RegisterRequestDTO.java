package org.instituteatri.deep.dtos.request;

public record RegisterRequestDTO(
        String name,
        String email,
        String password,
        String confirmPassword) {
}
