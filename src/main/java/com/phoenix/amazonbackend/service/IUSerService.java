package com.phoenix.amazonbackend.service;

import com.phoenix.amazonbackend.dtos.requestDtos.PasswordUpdateDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UpdateUserDetailsDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDto;
import com.phoenix.amazonbackend.dtos.responseDtos.ApiResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PageableResponse;
import com.phoenix.amazonbackend.utils.AllConstants.USER_FIELDS;

public interface IUSerService {
    /**
     * Creates New User account in Database
     *
     * @param userDto - userDto object
     * @return UserDto - userDto Object
     **/
    UserDto createUserService(final UserDto userDto);

    /**
     * Updates User Information either by UserId or UserName or PrimaryEmail
     *
     * @param user         - user object
     * @param userId       - id of user
     * @param userName     - username of user
     * @param primaryEmail - primary Email of user
     * @return UserDto - userDto Object
     **/
    UserDto updateUserServiceByUserIdOrUserNameOrPrimaryEmail(final UpdateUserDetailsDto user, final String userId,
                                                              final String userName, final String primaryEmail);

    /**
     * Deletes uses either by userId or UserName or PrimaryEmail
     *
     * @param userId       - id of user
     * @param userName     - username of user
     * @param primaryEmail - primary Email of user
     **/
    ApiResponse deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName,
                                                                  final String primaryEmail);

    /**
     * Fetch All Active users in DataBase
     *
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return PageableResponse<userDto> - page of userDto
     **/
    PageableResponse<UserDto> getAllUsers(final int pageNumber, final int pageSize,
                                          final String sortBy, final String sortDir);

    /**
     * Get user Information either by UserId or UserName or PrimaryEmail
     *
     * @param userId       - userid of user
     * @param userName     - username of user
     * @param primaryEmail - primary Email of user
     * @return UserDto     - userDto Object
     **/
    UserDto getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName,
                                                                      final String primaryEmail);

    /**
     * Searches a Particular User in Database by any one of
     * USER_NAME, FIRST_NAME, LAST_NAME, PRIMARY_EMAIL, SECONDARY_EMAIL, GENDER,
     * LAST_SEEN, ABOUT, PASSWORD, PROFILE_IMAGE
     *
     * @param field      - field of user entity
     * @param value      - value to query the field
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return PageableResponse<UserDto> - page of userDto
     **/
    PageableResponse<UserDto> searchUserByFieldAndValue(final USER_FIELDS field, final String value,
                                                        final int pageNumber, final int pageSize,
                                                        final USER_FIELDS sortBy, final String sortDir);

    /**
     * Searches all possible Users whose UserName matches by userNameWord
     *
     * @param userNameWord - username of user
     * @param pageNumber   - index value of page
     * @param pageSize     - size of page
     * @param sortBy       - sort column
     * @param sortDir      - direction of sorting
     * @return PageableResponse<UserDto> - page of userDto
     */
    PageableResponse<UserDto> searchAllUsersByUserName(final String userNameWord, final int pageNumber,
                                                       final int pageSize, final String sortBy,
                                                       final String sortDir);

    /**
     * Generates new passwords that User may use
     *
     * @return String
     **/
    String generatePasswordService();


    /**
     * Resets Password of a User
     *
     * @param passwordUpdateDto - request object to update password
     */
    void resetPasswordService(final PasswordUpdateDto passwordUpdateDto);
}
