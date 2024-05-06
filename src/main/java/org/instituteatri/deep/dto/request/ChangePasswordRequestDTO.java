package org.instituteatri.deep.dtos.request;

public record ChangePasswordRequestDTO(
        String oldPassword,
        String newPassword) {
}
