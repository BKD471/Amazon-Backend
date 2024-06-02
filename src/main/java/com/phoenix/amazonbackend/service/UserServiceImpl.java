package com.phoenix.amazonbackend.service;

import com.phoenix.amazonbackend.dtos.requestDtos.PasswordUpdateDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDetailsDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDto;
import com.phoenix.amazonbackend.dtos.responseDtos.ApiResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PageableResponse;
import com.phoenix.amazonbackend.entities.Users;
import com.phoenix.amazonbackend.repository.IUserRepository;
import com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.USER_FIELDS;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;


import static com.phoenix.amazonbackend.dtos.requestDtos.UserDetailsDto.userDetailsDtoToUsers;
import static com.phoenix.amazonbackend.dtos.requestDtos.UserDetailsDto.usersToUserDetailsDto;
import static com.phoenix.amazonbackend.dtos.requestDtos.UserDto.userDtoToUsers;
import static com.phoenix.amazonbackend.dtos.requestDtos.UserDto.usersToUsersDto;

@Service("UserServicePrimary")
public class UserServiceImpl extends AbstractService implements IUserService {
    private final IUserRepository userRepository;

    public UserServiceImpl(final IUserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUserService(final UserDto userDto) {
        // initialize user object with UUID as primary key & add password to passwordSet
        Users user = userDtoToUsers(userDto.initializeUser(userDto));

        // save user
        final Users savedUser = userRepository.save(user);
        return usersToUsersDto(savedUser);
    }

    @Override
    public UserDto updateUserServiceByUserIdOrUserNameOrPrimaryEmail(final UserDetailsDto userDetails,
                                                                     final UUID userId,
                                                                     final String userName,
                                                                     final String primaryEmail) {
        // Load user from DB
        final UserDetailsDto oldUser =
                usersToUserDetailsDto(
                        loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail));

        // update user data
        final UserDetailsDto updatedUserDetails = oldUser.updateUserDetails(oldUser, userDetails);

        // save
        final Users savedUser = userRepository.save(userDetailsDtoToUsers(updatedUserDetails));
        return usersToUsersDto(savedUser);
    }

    @Override
    public ApiResponse deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(final UUID userId,
                                                                         final String userName,
                                                                         final String primaryEmail) {
        userRepository.deleteByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);
        return ApiResponse.builder()
                .message(deleteResponseMessage(String.valueOf(userId), userName, primaryEmail))
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(final int pageNumber,
                                                 final int pageSize,
                                                 final String sortBy,
                                                 final String sortDir) {
        return null;
    }

    @Override
    public UserDto getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(final String userId,
                                                                             final String userName,
                                                                             final String primaryEmail) {
        return null;
    }

    @Override
    public PageableResponse<UserDto> searchUserByFieldAndValue(final USER_FIELDS field,
                                                               final String value,
                                                               final int pageNumber,
                                                               final int pageSize,
                                                               final USER_FIELDS sortBy,
                                                               final String sortDir) {
        return null;
    }

    @Override
    public PageableResponse<UserDto> searchAllUsersByUserName(final String userNameWord,
                                                              final int pageNumber,
                                                              final int pageSize,
                                                              final String sortBy,
                                                              final String sortDir) {
        return null;
    }

    @Override
    public String generatePasswordService() {
        final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String specialCase = "!@#$%^&*()-_+/<>?=|~";
        final String numbers = "0123456789";

        StringBuilder password = new StringBuilder();
        int capacity = 4, randomId = -1;
        for (int i = 0; i < 16; i++) {
            int k = i % capacity;

            switch (k) {
                case 0: {
                    randomId = (int) (Math.random() * lowerCase.length());
                    password.append(lowerCase.charAt(randomId));
                    break;
                }
                case 1: {
                    randomId = (int) (Math.random() * upperCase.length());
                    password.append(upperCase.charAt(randomId));
                    break;
                }
                case 2: {
                    randomId = (int) (Math.random() * specialCase.length());
                    password.append(specialCase.charAt(randomId));
                    break;
                }
                case 3: {
                    randomId = (int) (Math.random() * numbers.length());
                    password.append(numbers.charAt(randomId));
                    break;
                }
            }
        }
        return password.toString();
    }

    @Override
    public void resetPasswordService(final PasswordUpdateDto passwordUpdateDto) {
    }

    /***
     *  Preparing response messages for delete
     * ***/
    private String deleteResponseMessage(final String userId,
                                         final String userName,
                                         final String primaryEmail) {
        String field;
        String value;
        if (!StringUtils.isEmpty(userId)) {
            field = "userId";
            value = userId;
        } else if (!StringUtils.isEmpty(userName)) {
            field = "userName";
            value = userName;
        } else {
            field = "primaryEmail";
            value = primaryEmail;
        }
        return String.format("User with %s : %s is deleted successfully", field, value);
    }
}
