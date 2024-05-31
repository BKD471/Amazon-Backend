package com.phoenix.amazonbackend.dtos.responseDtos;

import lombok.Builder;

import java.time.LocalTime;
import java.util.Map;

@Builder
public record ErrorDetails(LocalTime timeStamp, String message, String details, Map<String, String> error) {
}
