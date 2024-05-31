package com.phoenix.amazonbackend.dtos.requestDtos;

import com.phoenix.amazonbackend.entities.Users;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.phoenix.amazonbackend.utils.GenderMapHelpers.getGender;

@Builder
public record UserDto(UUID userId,
                      String userName,
                      String firstName,
                      String lastName,
                      String primaryEmail,
                      String secondaryEmail,
                      String password,
                      String gender,
                      String profileImage,
                      String about,
                      LocalDateTime lastSeen,
                      LocalDateTime createdDate,
                      LocalDateTime modifiedDate,
                      String modifiedBy) {

    public UserDto mapToUsersDto(final Users users) {
        return UserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .profileImage(users.getProfileImage())
                .about(users.getAbout())
                .lastSeen(users.getLastSeen())
                .createdDate(users.getCreatedDate())
                .modifiedDate(users.getModifiedDate())
                .modifiedBy(users.getModifiedBy())
                .build();
    }

    public static Users mapToUsers(final UserDto userDto) {
        return Users.builder()
                .userId(userDto.userId())
                .userName(userDto.userName())
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .primaryEmail(userDto.primaryEmail())
                .secondaryEmail(userDto.secondaryEmail())
                .password(userDto.password())
                .gender(getGender(userDto.gender()))
                .profileImage(userDto.profileImage())
                .about(userDto.about())
                .build();
    }

    public Users mapToUsers(final UpdateUserDetailsDto updateUserDetailsDto) {
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

    public UpdateUserDetailsDto mapToUserDto(final Users users) {
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
