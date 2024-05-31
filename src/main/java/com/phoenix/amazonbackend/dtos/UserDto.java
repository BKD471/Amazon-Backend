package com.phoenix.amazonbackend.dtos;

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
                      LocalDateTime lastSeen) {

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
                .build();
    }

    public Users mapToUsers(final UserDto userDto) {
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
}
