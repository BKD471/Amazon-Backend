package com.phoenix.amazonbackend.dtos.requestDtos;

import lombok.Builder;

@Builder
public record PasswordUpdateDto(
        String primaryEmail,
        String oldPassword,
        String newPassword,
        String confirmPassword) {
}
