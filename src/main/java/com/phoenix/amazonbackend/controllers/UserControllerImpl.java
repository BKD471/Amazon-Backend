package com.phoenix.amazonbackend.controllers;


import com.phoenix.amazonbackend.dtos.requestDtos.PasswordUpdateDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UpdateUserDetailsDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDto;
import com.phoenix.amazonbackend.dtos.responseDtos.ApiResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PageableResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PasswordResponseMessages;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.phoenix.amazonbackend.utils.AllConstants.USER_FIELDS;


@RestController("UserControllerPrimary")
@AllArgsConstructor
public class UserControllerImpl implements IUserController {

    @Override
    public ResponseEntity<UserDto> createUser(final UserDto user) {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> updateUserByUserIdOrUserNameOrPrimaryEmail(final UpdateUserDetailsDto user,
                                                                              final String userId,
                                                                              final String userName,
                                                                              final String primaryEmail) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> deleteUserByUserIdOrUserNameOrPrimaryEmail(final String userId,
                                                                                  final String userName,
                                                                                  final String primaryEmail) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(final int pageNumber,
                                                                 final int pageSize,
                                                                 final String sortBy,
                                                                 final String sortDir) {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> getUserInformationByUserIdOrUserNameOrPrimaryEmail(final String userId,
                                                                                      final String userName,
                                                                                      final String primaryEmail) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<UserDto>> searchUserByFieldAndValue(final USER_FIELDS field,
                                                                               final String value,
                                                                               final int pageNumber,
                                                                               final int pageSize,
                                                                               final USER_FIELDS sortBy,
                                                                               final String sortDir) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<UserDto>> searchAllUsersByUserName(final String userNameWord,
                                                                              final int pageNumber,
                                                                              final int pageSize,
                                                                              final String sortBy,
                                                                              final String sortDir) {
        return null;
    }

    @Override
    public ResponseEntity<PasswordResponseMessages> generatePassword() {
        return null;
    }

    @Override
    public ResponseEntity<PasswordResponseMessages> resetMyPassword(final PasswordUpdateDto passwordUpdateDto) {
        return null;
    }
}
