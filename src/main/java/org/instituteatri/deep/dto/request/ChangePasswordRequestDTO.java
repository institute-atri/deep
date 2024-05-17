package org.instituteatri.deep.dto.request;

public record ChangePasswordRequestDTO(
        String oldPassword,
        String newPassword) {
}
