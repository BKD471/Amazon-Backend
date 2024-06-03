package com.phoenix.amazonbackend.service;

import com.phoenix.amazonbackend.config.ApplicationProperties;
import com.phoenix.amazonbackend.dtos.requestDtos.PasswordUpdateDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDetailsDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDto;
import com.phoenix.amazonbackend.dtos.responseDtos.ApiResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PageableResponse;
import com.phoenix.amazonbackend.entities.PassWordSet;
import com.phoenix.amazonbackend.entities.Users;
import com.phoenix.amazonbackend.repository.IUserRepository;
import com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.USER_FIELDS;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;


import static com.phoenix.amazonbackend.dtos.requestDtos.UserDetailsDto.userDetailsDtoToUsers;
import static com.phoenix.amazonbackend.dtos.requestDtos.UserDetailsDto.usersToUserDetailsDto;
import static com.phoenix.amazonbackend.dtos.requestDtos.UserDto.userDtoToUsers;
import static com.phoenix.amazonbackend.dtos.requestDtos.UserDto.usersToUsersDto;
import static com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.DestinationDtoType.USER_DTO;
import static com.phoenix.amazonbackend.utils.PagingUtils.getPageableResponse;

@Service("UserServicePrimary")
public class UserServiceImpl extends AbstractService implements IUserService {
    private final IUserRepository userRepository;
    private final ApplicationProperties applicationProperties;

    public UserServiceImpl(final IUserRepository userRepository,
                           final ApplicationProperties applicationProperties) {
        super(userRepository);
        this.userRepository = userRepository;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public UserDto createUserService(final UserDto userDto) {
        // initialize user object with UUID as primary key & add password to passwordSet
        final Users user = userDtoToUsers(userDto.initializeUser(userDto));

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
                        loadUserByUserIdOrUserNameOrPrimaryEmail(
                                userId,
                                userName,
                                primaryEmail
                        )
                );
        // update user data
        final UserDetailsDto updatedUserDetails = oldUser.updateUserDetails(oldUser, userDetails);
        // save
        final Users savedUser = userRepository.save(
                userDetailsDtoToUsers(updatedUserDetails)
        );
        return usersToUsersDto(savedUser);
    }

    @Override
    public ApiResponse deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(final UUID userId,
                                                                         final String userName,
                                                                         final String primaryEmail) throws IOException {
        // load user from db
        final Users fetchedUser = loadUserByUserIdOrUserNameOrPrimaryEmail(
                userId,
                userName,
                primaryEmail
        );
        // delete profile image of user if he/she has
        if (!StringUtils.isBlank(fetchedUser.getProfileImage())) {
            final String pathToProfileIMage = applicationProperties.getUserProfileImagePath() +
                    File.separator + fetchedUser.getProfileImage();
            Files.deleteIfExists(
                    Paths.get(pathToProfileIMage)
            );
        }
        userRepository.deleteByUserIdOrUserNameOrPrimaryEmail(
                userId,
                userName,
                primaryEmail
        );
        return ApiResponse.builder()
                .message(
                        deleteResponseMessage(
                                String.valueOf(userId),
                                userName,
                                primaryEmail
                        )
                )
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(final int pageNumber,
                                                 final int pageSize,
                                                 final String sortBy,
                                                 final String sortDir) {
        final Sort sort = sortDir.equals("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        // get the pageable object
        final Pageable pageableObject = getPageableObject(
                pageNumber,
                pageSize,
                sort
        );
        // get all user pages
        final Page<Users> userPage = userRepository.findAll(pageableObject);
        return getPageableResponse(userPage, USER_DTO);
    }

    @Override
    public UserDto getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(final UUID userId,
                                                                             final String userName,
                                                                             final String primaryEmail) {
        // load user from db & return as UserDto
        return usersToUsersDto(
                loadUserByUserIdOrUserNameOrPrimaryEmail(
                        userId,
                        userName,
                        primaryEmail
                )
        );
    }

    @Override
    public PageableResponse<UserDto> searchUserByFieldAndValue(final USER_FIELDS field,
                                                               final String value,
                                                               final int pageNumber,
                                                               final int pageSize,
                                                               final USER_FIELDS sortBy,
                                                               final String sortDir) {
        // get the user db column names
        final StringBuilder sortByColumn = getUserDbField(sortBy);
        // sort in either ascending or descending
        final Sort sort = sortDir.equals("desc") ?
                Sort.by(sortByColumn.toString()).descending() :
                Sort.by(sortByColumn.toString()).ascending();
        // get the pageable object
        final Pageable pageableObject = getPageableObject(
                pageNumber,
                pageSize,
                sort
        );
        Page<Users> usersPage = Page.empty();
        switch (field) {
            // get user by primary email
            case PRIMARY_EMAIL -> {
                usersPage = userRepository.searchUserByEmail(value, pageableObject).get();
            }
            // get user by user name
            case USER_NAME -> {
                usersPage = userRepository.searchUserByUserName(value, pageableObject).get();
            }
            // get all users by gender
            case GENDER -> {
                usersPage = userRepository.searchUserByGender(value, pageableObject).get();
            }
            // get all users by first name
            case FIRST_NAME -> {
                usersPage = userRepository.searchUserByFirstName(value, pageableObject).get();
            }
            // get all users by last name
            case LAST_NAME -> {
                usersPage = userRepository.searchUserByLastName(value, pageableObject).get();
            }
        }
        return getPageableResponse(usersPage, USER_DTO);
    }

    @Override
    public PageableResponse<UserDto> searchAllUsersByUserName(final String userNameWord,
                                                              final int pageNumber,
                                                              final int pageSize,
                                                              final String sortBy,
                                                              final String sortDir) {
        // sort in either ascending or descending
        final Sort sort = sortDir.equals("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        // get pageable object
        final Pageable pageableObject = getPageableObject(
                pageNumber,
                pageSize,
                sort
        );
        // get page of all user by matching userName
        final Page<Users> allUsersWithNearlyUserNamePage = userRepository
                .findAllByUserNameContaining(userNameWord, pageableObject).get();
        return getPageableResponse(allUsersWithNearlyUserNamePage, USER_DTO);
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
        final String primaryEmail = passwordUpdateDto.primaryEmail();
        // load user from db
        final Users fetchedUser = loadUserByUserIdOrUserNameOrPrimaryEmail(
                null,
                primaryEmail,
                primaryEmail
        );

        // update password & save
        final String newPassword = passwordUpdateDto.newPassword();
        final Set<PassWordSet> passWordSetSet = fetchedUser.getPrevious_password_set();
        PassWordSet newPassWordSet = PassWordSet
                .builder()
                .password_id(UUID.randomUUID())
                .passwords(newPassword)
                .users(fetchedUser).build();
        passWordSetSet.add(newPassWordSet);
        fetchedUser.setPrevious_password_set(passWordSetSet);
        fetchedUser.setPassword(newPassword);
        userRepository.save(fetchedUser);
    }

    /*
     * Preparing response messages for delete
     */
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

    /**
     * This returns a pageable object with pageNumber,pageSize,Sort
     */
    private Pageable getPageableObject(final int pageNumber,
                                       final int pageSize,
                                       final Sort sort) {
        return PageRequest.of(
                pageNumber - 1,
                pageSize,
                sort
        );
    }

    /**
     * This returns a db column
     */
    private StringBuilder getUserDbField(final USER_FIELDS sortBy) {
        final StringBuilder sortByColumn = new StringBuilder();
        switch (sortBy) {
            case USER_NAME -> sortByColumn.append("user_name");
            case FIRST_NAME -> sortByColumn.append("first_name");
            case LAST_NAME -> sortByColumn.append("last_name");
            case PRIMARY_EMAIL -> sortByColumn.append("user_primary_email");
            case SECONDARY_EMAIL -> sortByColumn.append("user_secondary_email");
            case PASSWORD -> sortByColumn.append("user_password");
            case GENDER -> sortByColumn.append("gender");
            case PROFILE_IMAGE -> sortByColumn.append("user_image_name");
            case LAST_SEEN -> sortByColumn.append("last_seen");
            case ABOUT -> sortByColumn.append("about");
        }
        return sortByColumn;
    }
}
