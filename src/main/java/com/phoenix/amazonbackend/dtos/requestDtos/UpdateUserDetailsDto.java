package com.phoenix.amazonbackend.dtos.requestDtos;

import com.phoenix.amazonbackend.entities.Users;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.phoenix.amazonbackend.utils.GenderMapingHelpers.getGender;

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
    public static Users mapToUsers(final UpdateUserDetailsDto updateUserDetailsDto) {
        return Users.builder()
                .userId(UUID.fromString(updateUserDetailsDto.userId()))
                .userName(updateUserDetailsDto.userName())
                .firstName(updateUserDetailsDto.firstName())
                .lastName(updateUserDetailsDto.lastName())
                .primaryEmail(updateUserDetailsDto.primaryEmail())
                .secondaryEmail(updateUserDetailsDto.secondaryEmail())
                .gender(getGender(updateUserDetailsDto.gender()))
                .about(updateUserDetailsDto.about())
                .build();
    }

    public static UpdateUserDetailsDto mapToUserDetailsDto(final Users users) {
        return UpdateUserDetailsDto.builder()
                .userId(String.valueOf(users.getUserId()))
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .about(users.getAbout())
                .createdDate(users.getCreatedDate())
                .modifiedBy(users.getModifiedBy())
                .modifiedDate(users.getModifiedDate())
                .build();
    }
}
