package com.phoenix.amazonbackend.controllers;


import com.phoenix.amazonbackend.dtos.requestDtos.PasswordUpdateDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDetailsDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDto;
import com.phoenix.amazonbackend.dtos.responseDtos.ApiResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PageableResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PasswordResponseMessages;
import com.phoenix.amazonbackend.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.USER_FIELDS;

import java.io.IOException;
import java.util.UUID;


@RestController("UserControllerPrimary")
@AllArgsConstructor
public class UserControllerImpl implements IUserController {
    private final IUserService userService;

    @Override
    public ResponseEntity<UserDto> createUser(final UserDto user) {
        final UserDto userDto = userService.createUserService(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserDto> updateUserByUserIdOrUserNameOrPrimaryEmail(final UserDetailsDto user,
                                                                              final String userId,
                                                                              final String userName,
                                                                              final String primaryEmail) {
        final UserDto userDto = userService
                .updateUserServiceByUserIdOrUserNameOrPrimaryEmail(
                        user,
                        UUID.fromString(userId),
                        userName,
                        primaryEmail
                );
        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteUserByUserIdOrUserNameOrPrimaryEmail(final String userId,
                                                                                  final String userName,
                                                                                  final String primaryEmail) throws IOException {
        final ApiResponse response = userService
                .deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(
                        UUID.fromString(userId),
                        userName,
                        primaryEmail
                );
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(final int pageNumber,
                                                                 final int pageSize,
                                                                 final String sortBy,
                                                                 final String sortDir) {
        final PageableResponse<UserDto> userDtoSet = userService
                .getAllUsers(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir
                );
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDto> getUserInformationByUserIdOrUserNameOrPrimaryEmail(final String userId,
                                                                                      final String userName,
                                                                                      final String primaryEmail) {
        final UserDto userDto = userService
                .getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(
                        UUID.fromString(userId),
                        userName,
                        primaryEmail
                );
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageableResponse<UserDto>> searchUserByFieldAndValue(final USER_FIELDS field,
                                                                               final String value,
                                                                               final int pageNumber,
                                                                               final int pageSize,
                                                                               final USER_FIELDS sortBy,
                                                                               final String sortDir) {
        final PageableResponse<UserDto> userDtoSet = userService
                .searchUserByFieldAndValue(
                        field,
                        value,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir
                );
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageableResponse<UserDto>> searchAllUsersByUserName(final String userNameWord,
                                                                              final int pageNumber,
                                                                              final int pageSize,
                                                                              final String sortBy,
                                                                              final String sortDir) {
        final PageableResponse<UserDto> userDtoSet = userService
                .searchAllUsersByUserName(
                        userNameWord,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir
                );
        return new ResponseEntity<>(userDtoSet, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PasswordResponseMessages> generatePassword() {
        final String password = userService.generatePasswordService();
        final PasswordResponseMessages passwordResponseMessages = PasswordResponseMessages.builder()
                .password(password)
                .message("Password generated")
                .success(true)
                .status(HttpStatus.OK).build();
        return new ResponseEntity<>(passwordResponseMessages, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PasswordResponseMessages> resetMyPassword(final PasswordUpdateDto passwordUpdateDto) {
        userService.resetPasswordService(passwordUpdateDto);
        final PasswordResponseMessages passwordResponseMessages = PasswordResponseMessages.builder()
                .message("Your password has been updated successfully")
                .success(true)
                .status(HttpStatus.ACCEPTED)
                .build();
        return new ResponseEntity<>(passwordResponseMessages, HttpStatus.ACCEPTED);
    }
}
