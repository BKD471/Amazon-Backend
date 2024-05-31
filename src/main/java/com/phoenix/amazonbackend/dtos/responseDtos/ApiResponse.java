package com.phoenix.amazonbackend.dtos.responseDtos;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ApiResponse(String message, boolean success, HttpStatus status) {
}
