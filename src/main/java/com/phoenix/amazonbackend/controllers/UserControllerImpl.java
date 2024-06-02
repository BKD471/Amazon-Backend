package com.phoenix.amazonbackend.controllers;


import com.phoenix.amazonbackend.dtos.requestDtos.PasswordUpdateDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UpdateUserDetailsDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDto;
import com.phoenix.amazonbackend.dtos.responseDtos.ApiResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PageableResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PasswordResponseMessages;
import com.phoenix.amazonbackend.utils.AllConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;



@RestController("UserControllerPrimary")
@AllArgsConstructor
public class UserControllerImpl implements IUserController {


    @Override
    public ResponseEntity<UserDto> createUser(UserDto user) {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> updateUserByUserIdOrUserNameOrPrimaryEmail(UpdateUserDetailsDto user, String userId, String userName, String primaryEmail) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> deleteUserByUserIdOrUserNameOrPrimaryEmail(String userId, String userName, String primaryEmail) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public ResponseEntity<UserDto> getUserInformationByUserIdOrUserNameOrPrimaryEmail(String userId, String userName, String primaryEmail) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<UserDto>> searchUserByFieldAndValue(AllConstants.USER_FIELDS field, String value, int pageNumber, int pageSize, AllConstants.USER_FIELDS sortBy, String sortDir) {
        return null;
    }

    @Override
    public ResponseEntity<PageableResponse<UserDto>> searchAllUsersByUserName(String userNameWord, int pageNumber, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public ResponseEntity<PasswordResponseMessages> generatePassword() {
        return null;
    }

    @Override
    public ResponseEntity<PasswordResponseMessages> resetMyPassword(PasswordUpdateDto passwordUpdateDto) {
        return null;
    }
}
