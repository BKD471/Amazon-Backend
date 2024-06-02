package com.phoenix.amazonbackend.dtos.requestDtos;

import com.phoenix.amazonbackend.entities.PassWordSet;
import com.phoenix.amazonbackend.entities.Users;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.phoenix.amazonbackend.utils.GenderMappingUtils.getGender;

@Builder
public record UserDto(String userId,
                      String userName,
                      String firstName,
                      String lastName,
                      String primaryEmail,
                      String secondaryEmail,
                      String password,
                      Set<PassWordSet> previous_password_set,
                      String gender,
                      String profileImage,
                      String about,
                      LocalDateTime lastSeen,
                      LocalDateTime createdDate,
                      LocalDateTime modifiedDate,
                      String modifiedBy) {

    public UserDto initializeUser(final UserDto users) {
        // initialize userId & trim leading or lagging whitespaces if any
        final String userIdUUID = UUID.randomUUID().toString();
        final String secondaryEmail = !Objects.isNull(secondaryEmail()) ? secondaryEmail().trim() : null;
        final String about = !Objects.isNull(about()) ? about().trim() : null;

        // register current password to used password set
        final Set<PassWordSet> passWordSetSet = new HashSet<>();
        passWordSetSet.add(PassWordSet.builder()
                .password_id(UUID.randomUUID())
                .passwords(password)
                .users(userDtoToUsers(users))
                .build());

        return new UserDto(
                userIdUUID,
                this.userName,
                this.firstName,
                this.lastName,
                this.primaryEmail,
                secondaryEmail,
                this.password,
                passWordSetSet,
                this.gender,
                this.profileImage,
                about,
                this.lastSeen,
                this.createdDate,
                this.modifiedDate,
                this.modifiedBy
        );
    }

    public static UserDto usersToUsersDto(final Users users) {
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

    public static Users userDtoToUsers(final UserDto userDto) {
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
