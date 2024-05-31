package com.phoenix.amazonbackend.dtos.responseDtos;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record PasswordResponseMessages(String password, String message, boolean success, HttpStatus status) {
}
