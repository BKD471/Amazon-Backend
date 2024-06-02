package com.phoenix.amazonbackend.dtos.requestDtos;

import com.phoenix.amazonbackend.entities.Users;
import io.micrometer.common.util.StringUtils;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.phoenix.amazonbackend.utils.GenderMappingUtils.getGender;


@Builder
public record UserDetailsDto(String userId,
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

    public UserDetailsDto updateUserDetails(final UserDetailsDto oldUserDetails,
                                               final UserDetailsDto newUserDetailDto) {

        // update userName
        final String userName = testCondition(oldUserDetails.userName(), newUserDetailDto.userName())
                ? newUserDetailDto.userName
                : oldUserDetails.userName;


        // update firstName
        final String firstName = testCondition(oldUserDetails.firstName(), newUserDetailDto.firstName())
                ? newUserDetailDto.firstName
                : oldUserDetails.firstName;

        // update firstName
        final String lastName = testCondition(oldUserDetails.lastName(), newUserDetailDto.lastName())
                ? newUserDetailDto.lastName
                : oldUserDetails.lastName;

        // update primaryEmail
        final String primaryEmail = testCondition(oldUserDetails.primaryEmail(), newUserDetailDto.primaryEmail())
                ? newUserDetailDto.primaryEmail
                : oldUserDetails.primaryEmail;

        // update firstName
        final String secondaryEmail = testCondition(oldUserDetails.secondaryEmail(), newUserDetailDto.secondaryEmail())
                ? newUserDetailDto.secondaryEmail
                : oldUserDetails.secondaryEmail;

        // update firstName
        final String gender = testCondition(oldUserDetails.gender(), newUserDetailDto.gender())
                ? newUserDetailDto.gender
                : oldUserDetails.gender;

        // update about
        final String about = testCondition(oldUserDetails.about(), newUserDetailDto.about())
                ? newUserDetailDto.about
                : oldUserDetails.about;

        return new UserDetailsDto(
                this.userId,
                userName,
                firstName,
                lastName,
                primaryEmail,
                secondaryEmail,
                gender,
                about,
                this.createdDate,
                this.modifiedDate,
                this.modifiedBy
        );
    }

    private boolean testCondition(final String oldField, final String newField) {
        Predicate<String> isNotBlankField = StringUtils::isNotBlank;
        BiPredicate<String, String> checkFieldEquality = String::equalsIgnoreCase;

        return isNotBlankField.test(newField) &&
                !checkFieldEquality.test(newField, oldField);
    }

    public static Users userDetailsDtoToUsers(final UserDetailsDto userDetailsDto) {
        return Users.builder()
                .userId(UUID.fromString(userDetailsDto.userId()))
                .userName(userDetailsDto.userName())
                .firstName(userDetailsDto.firstName())
                .lastName(userDetailsDto.lastName())
                .primaryEmail(userDetailsDto.primaryEmail())
                .secondaryEmail(userDetailsDto.secondaryEmail())
                .gender(getGender(userDetailsDto.gender()))
                .about(userDetailsDto.about())
                .build();
    }

    public static UserDetailsDto usersToUserDetailsDto(final Users users) {
        return UserDetailsDto.builder()
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
