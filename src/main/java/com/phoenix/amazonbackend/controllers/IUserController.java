package com.phoenix.amazonbackend.controllers;


import com.phoenix.amazonbackend.dtos.requestDtos.PasswordUpdateDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDetailsDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDto;
import com.phoenix.amazonbackend.dtos.responseDtos.ApiResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PageableResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PasswordResponseMessages;
import com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.USER_FIELDS;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping("/api/users")
public interface IUserController {

    /**
     * Handles User Creation request from Client
     *
     * @param user - User Object
     * @return ResponseEntity<UserDto> - UserDto Object
     */
    @PostMapping("/v1/create")
    ResponseEntity<UserDto> createUser(@RequestBody final UserDto user);

    /**
     * Handles User Information Updation Request from Client by UserId or UserName or PrimaryEmail
     *
     * @param user     - User Object
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<UserDto> - UserDto Object
     */
    @PutMapping("/v1/update")
    ResponseEntity<UserDto> updateUserByUserIdOrUserNameOrPrimaryEmail(@RequestBody final UserDetailsDto user,
                                                                       @RequestParam(value = "userId", required = false) final String userId,
                                                                       @RequestParam(value = "userName", required = false) final String userName,
                                                                       @RequestParam(value = "primaryEmail", required = false) final String primaryEmail);

    /**
     * Handles User Deletion Request from Client by UserId or UserName or PrimaryEmail
     *
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<ApiResponse> - ApiResponse Object
     */
    @DeleteMapping("/v1/delete")
    ResponseEntity<ApiResponse> deleteUserByUserIdOrUserNameOrPrimaryEmail(@RequestParam(value = "userId", required = false) final String userId,
                                                                           @RequestParam(value = "userName", required = false) final String userName,
                                                                           @RequestParam(value = "primaryEmail", required = false) final String primaryEmail);

    /**
     * Handles Fetch all users request from Client
     *
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDTo>> - page of userDtp
     */
    @GetMapping("/v1/getAll")
    ResponseEntity<PageableResponse<UserDto>> getAllUsers(@RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                          @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = "firstName", required = false) final String sortBy,
                                                          @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir);

    /**
     * Handles Fetch User Details request from Client
     *
     * @param primaryEmail - primary email of user
     * @param userName     - username of user
     * @return ResponseEntity<UserDto> - userDto Object
     */
    @GetMapping("/v1/info")
    ResponseEntity<UserDto> getUserInformationByUserIdOrUserNameOrPrimaryEmail(@RequestParam(value = "userId", required = false) final String userId,
                                                                               @RequestParam(value = "userName", required = false) final String userName,
                                                                               @RequestParam(value = "primaryEmail", required = false) final String primaryEmail);

    /**
     * Handles Search an User request from Client based on any one of
     * USER_NAME, FIRST_NAME, LAST_NAME, PRIMARY_EMAIL, SECONDARY_EMAIL,
     * GENDER, LAST_SEEN, ABOUT, PASSWORD, PROFILE_IMAGE
     *
     * @param field      - field of User Entity
     * @param value      - value of field
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDto>> - list of UserDto
     */
    @GetMapping("/v1/search_by_field")
    ResponseEntity<PageableResponse<UserDto>> searchUserByFieldAndValue(@RequestParam(value = "field") final USER_FIELDS field,
                                                                        @RequestParam(value = "value") final String value,
                                                                        @RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                                        @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                                        @RequestParam(value = "sortBy", defaultValue = "FIRST_NAME", required = false) final USER_FIELDS sortBy,
                                                                        @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir);

    /**
     * Handles search User request from client based on userNameWord which matches to existing userName on DB
     *
     * @param userNameWord - Keyword to get multiple users with almost same name initials
     * @param pageNumber   - index value of page
     * @param pageSize     - size of page
     * @param sortBy       - sort column
     * @param sortDir      - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDto>> - list of userDto
     */
    @GetMapping("/v1/search_by_username/{userNameWord}")
    ResponseEntity<PageableResponse<UserDto>> searchAllUsersByUserName(@PathVariable("userNameWord") final String userNameWord,
                                                                       @RequestParam(value = "pageNumber", defaultValue = "1", required = false) final int pageNumber,
                                                                       @RequestParam(value = "pageSize", defaultValue = "5", required = false) final int pageSize,
                                                                       @RequestParam(value = "sortBy", defaultValue = "firstName", required = false) final String sortBy,
                                                                       @RequestParam(value = "sortDir", defaultValue = "asc", required = false) final String sortDir);


    /**
     * Handles generate password request from Client that complies the password guidelines
     *
     * @return PasswordResponseMessages
     **/
    @GetMapping("/v1/genPassword")
    ResponseEntity<PasswordResponseMessages> generatePassword();


    /*
     * currently resetting password when password is lost is not developed
     * you have to know your old password to reset it
     * OTP/email based password resetting will be done later
     * Handles password reset request from Client
     *
     * @param passwordUpdateDto - object to update old password
     * @return PasswordResponseMessages
     **/
    @PatchMapping("/v1/reset/password")
    ResponseEntity<PasswordResponseMessages> resetMyPassword(@RequestBody final PasswordUpdateDto passwordUpdateDto);
}
