package com.phoenix.amazonbackend.dtos.requestDtos;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateUserDetailsDto(String userId,
                                   String userName,
                                   String firstName,
                                   String lastName,
                                   String primaryEmail,
                                   String secondaryEmail,
                                   String gender,
                                   String about,
                                   LocalDateTime createdDate,
                                   LocalDateTime modifiedDate,
                                   String modifiedBy
) {
}
