package com.phoenix.amazonbackend.service;

import com.phoenix.amazonbackend.dtos.requestDtos.PasswordUpdateDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UpdateUserDetailsDto;
import com.phoenix.amazonbackend.dtos.requestDtos.UserDto;
import com.phoenix.amazonbackend.dtos.responseDtos.ApiResponse;
import com.phoenix.amazonbackend.dtos.responseDtos.PageableResponse;
import com.phoenix.amazonbackend.entities.PassWordSet;
import com.phoenix.amazonbackend.entities.Users;
import com.phoenix.amazonbackend.repository.IUserRepository;
import com.phoenix.amazonbackend.utils.AllConstants;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.phoenix.amazonbackend.dtos.requestDtos.UserDto.mapToUsers;
import static com.phoenix.amazonbackend.dtos.requestDtos.UserDto.mapToUsersDto;

@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;

    @Override
    public UserDto createUserService(final UserDto userDto) {
        final String methodName = "createUser(UserDto) in UserServiceImpl";
        // initialize user object with UUID as primary key & also to trim leading or lagging whitespaces if any
        final Users user = mapToUsers(userDto);
        Users userWithId = initializeUserId(user);

        // adding the password to set of password
        userWithId = addPasswordToPreviousPasswordSet(userWithId, userWithId.getPassword());

        // save user
        final Users savedUser = userRepository.save(userWithId);
        return mapToUsersDto(savedUser);
    }

    @Override
    public UserDto updateUserServiceByUserIdOrUserNameOrPrimaryEmail(UpdateUserDetailsDto user, String userId, String userName, String primaryEmail) {
        return null;
    }

    @Override
    public ApiResponse deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(String userId, String userName, String primaryEmail) {
        return null;
    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public UserDto getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(String userId, String userName, String primaryEmail) {
        return null;
    }

    @Override
    public PageableResponse<UserDto> searchUserByFieldAndValue(AllConstants.USER_FIELDS field, String value, int pageNumber, int pageSize, AllConstants.USER_FIELDS sortBy, String sortDir) {
        return null;
    }

    @Override
    public PageableResponse<UserDto> searchAllUsersByUserName(String userNameWord, int pageNumber, int pageSize, String sortBy, String sortDir) {
        return null;
    }

    @Override
    public String generatePasswordService() {
        return null;
    }

    @Override
    public void resetPasswordService(PasswordUpdateDto passwordUpdateDto) {

    }

    private Users initializeUserId(final Users user) {
        // trimming leading & lagging whitespaces if any
        final UUID userIdUUID = UUID.randomUUID();
        final String secondaryEmail = StringUtils.isBlank(user.getSecondaryEmail()) ? user.getSecondaryEmail() : user.getSecondaryEmail().trim();
        final String about = StringUtils.isBlank(user.getAbout()) ? user.getAbout() : user.getAbout().trim();
        final String password = StringUtils.isBlank(user.getPassword()) ? user.getPassword() : user.getPassword().trim();
        final String userName = StringUtils.isBlank(user.getUserName()) ? user.getUserName() : user.getUserName().trim();
        final String primaryEmail = StringUtils.isBlank(user.getPrimaryEmail()) ? user.getPrimaryEmail() : user.getPrimaryEmail().trim();
        final AllConstants.GENDER gender = user.getGender();
        final String firstName = StringUtils.isBlank(user.getFirstName()) ? user.getFirstName() : user.getFirstName().trim();
        final String lastName = StringUtils.isBlank(user.getLastName()) ? user.getLastName() : user.getLastName().trim();

        return Users.builder()
                .userId(userIdUUID)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .primaryEmail(primaryEmail)
                .secondaryEmail(secondaryEmail)
                .gender(gender)
                .password(password)
                .about(about)
                .lastSeen(LocalDateTime.now())
                .modifiedBy(null)
                .modifiedDate(null)
                .build();
    }

    private Users addPasswordToPreviousPasswordSet(final Users users, final String password) {
        Set<PassWordSet> oldPassWordSet = users.getPrevious_password_set();
        if (CollectionUtils.isEmpty(oldPassWordSet)) oldPassWordSet = new HashSet<>();
        final PassWordSet newPassWordSet = PassWordSet.builder()
                .password_id(UUID.randomUUID())
                .passwords(password)
                .users(users).build();
        oldPassWordSet.add(newPassWordSet);
        return Users.builder()
                .previous_password_set(oldPassWordSet)
                .password(users.getPassword())
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender())
                .about(users.getAbout())
                .profileImage(users.getProfileImage())
                .lastSeen(users.getLastSeen())
                .createdDate(users.getCreatedDate())
                .modifiedDate(null)
                .modifiedBy(null)
                .build();
    }
}
