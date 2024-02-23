package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.requestDtos.AddressRequestDto;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UserCreatedRequestDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.AddressResponseDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.ApiResponse;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.PageableResponse;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.PasswordUpdateDto;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UpdateUserDto;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UserDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.UserCreatedResponseDto;
import com.phoenix.amazon.AmazonBackend.entity.Address;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.AbstractService;
import com.phoenix.amazon.AmazonBackend.services.IUserService;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.PASSWORD;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.SET_ADDRESS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.FIRST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.LAST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.ABOUT;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.SECONDARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.DELETE_USER_BY_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.CREATE_USER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_USERNAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_SECONDARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.UPDATE_PASSWORD;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_ALL_USERS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_USER_BY_PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_USER_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_FIRST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_LAST_NAME;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.SEARCH_ALL_USERS_BY_GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.VALIDATE_PASSWORD;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.AddressResponseDtoToAddress;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserToUserCreatedResponseDto;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserUpdateDtoToUsers;
import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UsersToUsersDto;
import static com.phoenix.amazon.AmazonBackend.helpers.PagingHelpers.getPageableResponse;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.DestinationDtoType.USER_DTO;


@Service("UserServicePrimary")
public class UserServiceImpl extends AbstractService implements IUserService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;
    private final String userImagePath;
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(IUserRepository userRepository,
                           IUserValidationService userValidationService,
                           @Value("${path.services.image.properties}") final String PATH_TO_IMAGE_PROPS) {
        super(userRepository, userValidationService);
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;

        final Properties properties = new Properties();
        try {
            // load the properties file
            properties.load(new FileInputStream(PATH_TO_IMAGE_PROPS));
        } catch (IOException e) {
            logger.error("Error in reading the props in {} UserServiceImpl", e.getMessage());
        }
        // get the value from the keys of properties file
        this.userImagePath = properties.getProperty("user.profile.images.path");
    }

    private UserDto processUser(final UserDto userDto) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException {
        // validate null object
        validateNullField(userDto, "User object is null",
                "processUser in UserService");

        // trimming leading & lagging whitespaces if any
        final String userIdUUID = UUID.randomUUID().toString();
        final String secondaryEmail = StringUtils.isBlank(userDto.secondaryEmail()) ? userDto.secondaryEmail() : userDto.secondaryEmail().trim();
        final String about = StringUtils.isBlank(userDto.about()) ? userDto.about() : userDto.about().trim();
        final String password = userDto.password().trim();
        final String userName = userDto.userName().trim();
        final String primaryEmail = userDto.primaryEmail().trim();
        final String gender = userDto.gender().trim();
        final String firstName = userDto.firstName().trim();
        final String lastName = userDto.lastName().trim();

        return new UserDto.builder()
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
                .build();
    }

    private AddressResponseDto processAddress(final AddressRequestDto addressRequestDto) throws BadApiRequestExceptions {
        // validate null object
        validateNullField(addressRequestDto, "address object is null",
                "processAddress in UserService");

        final String addressId = UUID.randomUUID().toString();
        final String addressLineOne = addressRequestDto.addressLineNumberOne().trim();
        final String addressLineTwo = addressRequestDto.addressLineNumberTwo().trim();
        final String pinCode = addressRequestDto.pinCode().trim();
        final String mobileNumber = addressRequestDto.mobileNumber().trim();
        final String townOrCity = addressRequestDto.townOrCity().trim();

        // TODO call third party service to get lat,longitude,state,district,country


        return new AddressResponseDto.builder()
                .addressId(addressId)
                .addressLineNumberOne(addressLineOne)
                .addressLineNumberTwo(addressLineTwo)
                .addressType(addressRequestDto.addressType())
                .pinCode(pinCode)
                .mobileNumber(mobileNumber)
                .townOrCity(townOrCity)
                .latitude("DUMMY")
                .longitude("DUMMY")
                .state("DUMMY")
                .district("DUMMY")
                .country("DUMMY")
                .build();
    }

    /***
     *  Preparing response messages for delete
     * ***/
    private String deleteResponseMessage(final String userId, final String userName, final String primaryEmail) {
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
     * This returns a pagebale object with pageNumber,pageSize,Sort
     ***/
    private Pageable getPageableObject(final int pageNumber, final int pageSize, final Sort sort) {
        return PageRequest.of(pageNumber - 1, pageSize, sort);
    }

    /**
     * @param userDto - userDto object
     * @return UserDto - userDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    @Override
    public UserCreatedResponseDto createUserService(final UserCreatedRequestDto userCreatedRequestDto) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException {
        final String methodName = "createUser(UserDto) in UserServiceImpl";
        // initialize user object with UUID as primary key & also to trim leading or lagging whitespaces if any
        UserDto userDtoWithId = processUser(userCreatedRequestDto.userDto());
        Users user = UserDtoToUsers(userDtoWithId);

        AddressRequestDto addressRequestDto = userCreatedRequestDto.addressRequestDto();
        AddressResponseDto addressResponseDto = processAddress(addressRequestDto);
        Address address = AddressResponseDtoToAddress(addressResponseDto, user);

        // validate path for create user
        userValidationService.validateUser(Optional.of(user), Optional.empty(), methodName, CREATE_USER);

        // adding the password to set of password
        user = constructUser(user, user, PASSWORD);

        // add the address
        Users newUser = new Users.builder().address_set(Set.of(address)).build();
        user = constructUser(user, newUser, SET_ADDRESS);

        // save user
        Users savedUser = userRepository.save(user);
        return UserToUserCreatedResponseDto(savedUser);
    }

    /**
     * @param user         - user object
     * @param userId       - id of user
     * @param userName     - username of user
     * @param primaryEmail - primary Email of user
     * @return UserDto - userDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    @Override
    public UserDto updateUserServiceByUserIdOrUserNameOrPrimaryEmail(final UpdateUserDto user, final String userId, final String userName, final String primaryEmail) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        final String methodName = "updateUserByUserIdOrUserName(UserDto,String) in UserServiceImpl";
        Users userDetails = UserUpdateDtoToUsers(user);
        // Load user from DB
        Users fetchedUser = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail, methodName);

        Predicate<String> isNotBlankField = StringUtils::isNotBlank;
        BiPredicate<String, String> checkFieldEquality = String::equalsIgnoreCase;
        Predicate<GENDER> isNotBlankFieldEnum = Objects::nonNull;
        BiPredicate<GENDER, GENDER> checkEqualEnumValues = Objects::equals;

        // update userName
        if (isNotBlankField.test(userDetails.getUserName()) &&
                !checkFieldEquality.test(userDetails.getUserName(), fetchedUser.getUserName())) {
            // validation to update userName
            userValidationService.validateUser(Optional.of(userDetails), Optional.of(fetchedUser), methodName, UPDATE_USERNAME);
            fetchedUser = constructUser(fetchedUser, userDetails, USER_NAME);
        }
        // update firstName
        if (isNotBlankField.test(userDetails.getFirstName()) &&
                !checkFieldEquality.test(userDetails.getFirstName(), fetchedUser.getFirstName())) {
            fetchedUser = constructUser(fetchedUser, userDetails, FIRST_NAME);
        }
        // update LastName
        if (isNotBlankField.test(userDetails.getLastName()) &&
                !checkFieldEquality.test(userDetails.getLastName(), fetchedUser.getLastName())) {
            fetchedUser = constructUser(fetchedUser, userDetails, LAST_NAME);
        }
        // update primaryEmail
        if (isNotBlankField.test(userDetails.getPrimaryEmail()) &&
                !checkFieldEquality.test(userDetails.getPrimaryEmail(), fetchedUser.getPrimaryEmail())) {
            // validate update primaryEmail
            userValidationService.validateUser(Optional.of(userDetails), Optional.of(fetchedUser), methodName, UPDATE_PRIMARY_EMAIL);
            fetchedUser = constructUser(fetchedUser, userDetails, PRIMARY_EMAIL);
        }
        // update secondaryEmail
        if (isNotBlankField.test(userDetails.getSecondaryEmail()) &&
                !checkFieldEquality.test(userDetails.getSecondaryEmail(), fetchedUser.getSecondaryEmail())) {
            // validate update secondaryEmail
            userValidationService.validateUser(Optional.of(userDetails), Optional.of(fetchedUser), methodName, UPDATE_SECONDARY_EMAIL);
            fetchedUser = constructUser(fetchedUser, userDetails, SECONDARY_EMAIL);
        }
        // update gender
        if (isNotBlankFieldEnum.test(userDetails.getGender()) &&
                !checkEqualEnumValues.test(userDetails.getGender(), fetchedUser.getGender())) {
            fetchedUser = constructUser(fetchedUser, userDetails, GENDER);
        }
        // update about
        if (isNotBlankField.test(userDetails.getAbout()) &&
                !checkFieldEquality.test(userDetails.getAbout(), fetchedUser.getAbout())) {
            fetchedUser = constructUser(fetchedUser, userDetails, ABOUT);
        }

        // save
        Users savedUser = userRepository.save(fetchedUser);
        return UsersToUsersDto(savedUser);
    }

    /**
     * @param userId       - id of user
     * @param userName     - username of user
     * @param primaryEmail - primary Email of user
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    @Override
    public ApiResponse deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException {
        final String methodName = "deleteUserByUserIdOrUserName(string) in UserServiceImpl";
        // load user from db
        Users fetchedUser = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail, methodName);
        // validate delete user by P0 fields
        userValidationService.validateUser(Optional.empty(), Optional.of(fetchedUser), methodName, DELETE_USER_BY_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL);

        // delete profile image of user if he/she has
        if (!StringUtils.isBlank(fetchedUser.getProfileImage())) {
            final String pathToProfileIMage = userImagePath + File.separator + fetchedUser.getProfileImage();
            Files.deleteIfExists(Paths.get(pathToProfileIMage));
        }

        userRepository.deleteByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);
        return new ApiResponse.builder()
                .message(deleteResponseMessage(userId, userName, primaryEmail))
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return PageableResponse<userDto> - page of userDto
     * @throws UserNotFoundExceptions - list of exceptions being thrown
     **/
    @Override
    public PageableResponse<UserDto> getAllUsers(final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions {
        final String methodName = "getALlUsers() in UserServiceImpl";
        final Sort sort = sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        // get the pageable object
        final Pageable pageableObject = getPageableObject(pageNumber, pageSize, sort);
        // get all user pages
        Page<Users> userPage = userRepository.findAll(pageableObject);
        // validate user page is empty or not
        userValidationService.validateUserList(userPage.getContent(), methodName, GET_ALL_USERS);
        return getPageableResponse(userPage, USER_DTO);
    }

    /**
     * @param userId       - userid of user
     * @param userName     - username of user
     * @param primaryEmail - primary Email of user
     * @return UserDto     - userDto Object
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException -list of exceptions being thrown
     **/
    @Override
    public UserDto getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail) throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException {
        final String methodName = "getUserInformationByEmailOrUserName(String) in UserServiceImpl";
        // load user from db
        Users users = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail, methodName);
        return UsersToUsersDto(users);
    }

    /**
     * @param field      - field of user entity
     * @param value      - value to query the field
     * @param pageNumber - index value of page
     * @param pageSize   - size of page
     * @param sortBy     - sort column
     * @param sortDir    - direction of sorting
     * @return PageableResponse<UserDto> - page of userDto
     * @throws UserNotFoundExceptions - list of exceptions being thrown
     **/
    @Override
    public PageableResponse<UserDto> searchUserByFieldAndValue(final USER_FIELDS field, final String value, final int pageNumber, final int pageSize, final USER_FIELDS sortBy, final String sortDir) throws UserNotFoundExceptions {
        final String methodName = "searchUserByFieldAndValue(field,String) in UserServiceImpl";
        // get the user db column names
        final StringBuffer sortByColumn = getUserDbField(sortBy);
        // sort in either ascending or descending
        final Sort sort = sortDir.equals("desc") ? Sort.by(sortByColumn.toString()).descending() : Sort.by(sortByColumn.toString()).ascending();
        // get the pageable object
        final Pageable pageableObject = getPageableObject(pageNumber, pageSize, sort);
        Page<Users> usersPage = Page.empty();
        switch (field) {
            // get user by primary email
            case PRIMARY_EMAIL -> {
                usersPage = userRepository.searchUserByEmail(value, pageableObject).get();
                userValidationService.validateUserList(usersPage.getContent(), methodName, SEARCH_USER_BY_PRIMARY_EMAIL);
            }
            // get user by user name
            case USER_NAME -> {
                usersPage = userRepository.searchUserByUserName(value, pageableObject).get();
                userValidationService.validateUserList(usersPage.getContent(), methodName, SEARCH_ALL_USERS_BY_USER_NAME);
            }
            // get all users by gender
            case GENDER -> {
                usersPage = userRepository.searchUserByGender(value, pageableObject).get();
                userValidationService.validateUserList(usersPage.getContent(), methodName, SEARCH_ALL_USERS_BY_GENDER);
            }
            // get all users by first name
            case FIRST_NAME -> {
                usersPage = userRepository.searchUserByFirstName(value, pageableObject).get();
                userValidationService.validateUserList(usersPage.getContent(), methodName, SEARCH_ALL_USERS_BY_FIRST_NAME);
            }
            // get all users by last name
            case LAST_NAME -> {
                usersPage = userRepository.searchUserByLastName(value, pageableObject).get();
                userValidationService.validateUserList(usersPage.getContent(), methodName, SEARCH_ALL_USERS_BY_LAST_NAME);
            }
        }
        return getPageableResponse(usersPage, USER_DTO);
    }

    /**
     * @param userNameWord - username of user
     * @param pageNumber   - index value of page
     * @param pageSize     - size of page
     * @param sortBy       - sort column
     * @param sortDir      - direction of sorting
     * @return PageableResponse<UserDto> - page of userDto
     * @throws UserNotFoundExceptions - list of exceptions being thrown
     */
    @Override
    public PageableResponse<UserDto> searchAllUsersByUserName(final String userNameWord, final int pageNumber, final int pageSize, final String sortBy, final String sortDir) throws UserNotFoundExceptions {
        final String methodName = "searchAllUsersByUserName(string) in UsersServiceImpl";
        // sort in either ascending or descending
        final Sort sort = sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        // get pageable object
        final Pageable pageableObject = getPageableObject(pageNumber, pageSize, sort);
        // get page of all user by matching userName
        Page<Users> allUsersWithNearlyUserNamePage = userRepository.findAllByUserNameContaining(userNameWord, pageableObject).get();
        // validate to know whether user page is empty or not
        userValidationService.validateUserList(allUsersWithNearlyUserNamePage.getContent(), methodName, SEARCH_ALL_USERS_BY_USER_NAME);
        return getPageableResponse(allUsersWithNearlyUserNamePage, USER_DTO);
    }

    /**
     * this service returning a password of more or equals to 16 characters with one uppercase,lowercase,special characters
     * and numbers
     **/
    @Override
    public String generatePasswordService() {
        final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String specialCase = "!@#$%^&*()-_+/<>?=|";
        final String numbers = "0123456789";

        StringBuffer password = new StringBuffer();
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

    /**
     * @param passwordUpdateDto - request object to update password
     * @throws UserNotFoundExceptions,UserExceptions,BadApiRequestExceptions,IOException - list of exceptions to be thrown
     */
    @Override
    public void resetPasswordService(final PasswordUpdateDto passwordUpdateDto) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        final String methodName = "resetPasswordService(primaryEmail) in UserServiceImpl";
        final String primaryEmail = passwordUpdateDto.primaryEmail();
        // load user from db
        Users fetchedUser = loadUserByUserIdOrUserNameOrPrimaryEmail(primaryEmail, primaryEmail, primaryEmail, methodName);
        // check is the old password , the current password of user
        final String oldPassword = passwordUpdateDto.oldPassword();
        Users oldUser = new Users.builder().password(oldPassword).build();
        // validate to check old and current password matching
        userValidationService.validateUser(Optional.of(oldUser), Optional.of(fetchedUser), methodName, VALIDATE_PASSWORD);
        //update password & save
        final String newPassword = passwordUpdateDto.newPassword();
        Users newUser = new Users.builder().password(newPassword).build();
        // validate to check if new password had been used before
        userValidationService.validateUser(Optional.of(newUser), Optional.of(fetchedUser), methodName, UPDATE_PASSWORD);
        fetchedUser = constructUser(fetchedUser, newUser, PASSWORD);
        userRepository.save(fetchedUser);
    }
}