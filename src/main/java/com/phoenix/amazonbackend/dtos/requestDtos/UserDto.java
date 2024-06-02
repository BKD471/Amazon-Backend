package com.phoenix.amazonbackend.dtos.requestDtos;

import com.phoenix.amazonbackend.entities.Users;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.phoenix.amazonbackend.utils.GenderMapingHelpers.getGender;

@Builder
public record UserDto(String userId,
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

    public UserDto updateUserId() {
        final String userIdUUID = UUID.randomUUID().toString();
        final String secondaryEmail = !Objects.isNull(secondaryEmail()) ? secondaryEmail().trim() : null;
        final String about = !Objects.isNull(about()) ? about().trim() : null;
        return new UserDto(
                userIdUUID,
                this.userName,
                this.firstName,
                this.lastName,
                this.primaryEmail,
                secondaryEmail,
                this.password,
                this.gender,
                this.profileImage,
                about,
                this.lastSeen,
                this.createdDate,
                this.modifiedDate,
                this.modifiedBy
        );
    }

    public static UserDto mapToUsersDto(final Users users) {
        return UserDto.builder()
                .userId(String.valueOf(users.getUserId()))
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
                .userId(UUID.fromString(userDto.userId()))
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
