package com.phoenix.amazon.AmazonBackend.controllers.impl;

import com.phoenix.amazon.AmazonBackend.controllers.IUserController;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UserCreatedRequestDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.ImageResponseMessages;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.PageableResponse;

import com.phoenix.amazon.AmazonBackend.dto.responseDtos.PasswordResponseMessages;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.PasswordUpdateDto;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UpdateUserDto;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UserDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.UserCreatedResponseDto;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.services.IImageService;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;

import com.phoenix.amazon.AmazonBackend.services.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@RestController("UserControllerPrimary")
public class UserControllerImpl implements IUserController {
    private final IUserService userService;
    private final IImageService imageService;

    UserControllerImpl(IUserService userService, IImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    /**
     * @param user - User Object
     * @return ResponseEntity<UserDto> - UserDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     */
    @Override
    public ResponseEntity<UserCreatedResponseDto> createUser(final UserCreatedRequestDto createUserRequestDto) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        UserCreatedResponseDto userDto = userService.createUserService(createUserRequestDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    /**
     * @param user     - User Object
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<UserDto> - UserDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     */
    @Override
    public ResponseEntity<UserDto> updateUserByUserIdOrUserNameOrPrimaryEmail(final UpdateUserDto user, final String userId, final String userName, final String primaryEmail) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        UserDto userDto = userService.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(user, userId, userName, primaryEmail);
        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }


    /**
     * @param userId   - User Id
     * @param userName - userName of user
     * @return ResponseEntity<ApiResponse> - ApiResponse Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     */
    @Override
    public ResponseEntity<ApiResponse> deleteUserByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        ApiResponse response = userService.deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    /**
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDTo>> - page of userDtp
     * @throws UserNotFoundExceptions -list of exceptions being thrown
     */
    @Override
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions {
        PageableResponse<UserDto> userDtoSet = userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }

    /**
     * @param primaryEmail - primary email of user
     * @param userName     - username of user
     * @return ResponseEntity<UserDto> - userDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     */
    @Override
    public ResponseEntity<UserDto> getUserInformationByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        UserDto userDto = userService.getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @param field      - field of User Entity
     * @param value      - value of field
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDto>> - list of UserDto
     * @throws UserNotFoundExceptions -list of exceptions being thrown
     */
    @Override
    public ResponseEntity<PageableResponse<UserDto>> searchUserByFieldAndValue(final USER_FIELDS field, final String value, final int pageNumber, final int pageSize, final USER_FIELDS sortBy, final String sortDir) throws UserNotFoundExceptions {
        PageableResponse<UserDto> userDtoSet = userService.searchUserByFieldAndValue(field, value, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }

    /**
     * @param userNameWord - Keyword to get multiple users with almost same name initials
     * @param pageNumber   - index value of page
     * @param pageSize     - size of page
     * @param sortBy       - sort column
     * @param sortDir      - direction of sorting
     * @return ResponseEntity<PageableResponse < UserDto>> - list of userDto
     * @throws UserNotFoundExceptions -list of exceptions being thrown
     */
    @Override
    public ResponseEntity<PageableResponse<UserDto>> searchAllUsersByUserName(final String userNameWord, final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions {
        PageableResponse<UserDto> userDtoSet = userService.searchAllUsersByUserName(userNameWord, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }

    /**
     * @param image        - profile image of user
     * @param primaryEmail - primary eMail of user
     * @param userName     - userName of user
     * @return ResponseEntity<ImageResponseMessages> - image response
     * @throws IOException,BadApiRequestExceptions,UserNotFoundExceptions,UserExceptions - list of exceptions being thrown
     */
    @Override
    public ResponseEntity<ImageResponseMessages> uploadUserImageByUserIdOrUserNameOrPrimaryEmail(final MultipartFile image,
                                                                                                 final String userId,
                                                                                                 final String userName,
                                                                                                 final String primaryEmail) throws IOException, BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions {
        final String imageName = imageService.uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(image, userId, userName, primaryEmail);
        ImageResponseMessages imageResponseMessages =
                new ImageResponseMessages.Builder()
                        .imageName(imageName)
                        .message("Profile image has been uploaded successfully")
                        .status(HttpStatus.ACCEPTED)
                        .success(true)
                        .build();
        return new ResponseEntity<>(imageResponseMessages, HttpStatus.ACCEPTED);
    }

    /**
     * @param primaryEmail - primary email of user
     * @param userName     - userName of user
     * @param response     - http response
     * @throws IOException,UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions - list of exceptions being thrown
     */
    @Override
    public void serveUserImageByUserIdOrUserNameOrPrimaryEmail(final String userId,
                                                               final String userName,
                                                               final String primaryEmail,
                                                               final HttpServletResponse response) throws IOException, UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions {
        InputStream resource = imageService.serveUserImageServiceByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

    /**
     * @return PasswordResponseMessages
     **/
    @Override
    public ResponseEntity<PasswordResponseMessages> generatePassword() {
        final String password = userService.generatePasswordService();
        PasswordResponseMessages passwordResponseMessages = new PasswordResponseMessages.Builder()
                .password(password)
                .message("Password generated")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(passwordResponseMessages, HttpStatus.OK);
    }

    /**
     * currently resetting password when password is lost is not developed
     * you have to know your old password to reset it
     * OTP/email based password resetting will be done later
     */
    /**
     * @param passwordUpdateDto - object to update old password
     * @return PasswordResponseMessages
     **/
    @Override
    public ResponseEntity<PasswordResponseMessages> resetMyPassword(final PasswordUpdateDto passwordUpdateDto) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        userService.resetPasswordService(passwordUpdateDto);
        PasswordResponseMessages passwordResponseMessages = new PasswordResponseMessages.Builder()
                .message("Your password has been updated successfully")
                .success(true)
                .status(HttpStatus.ACCEPTED)
                .build();
        return new ResponseEntity<>(passwordResponseMessages, HttpStatus.ACCEPTED);
    }
}
