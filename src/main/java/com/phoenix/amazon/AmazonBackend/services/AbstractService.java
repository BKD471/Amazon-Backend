package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.entity.Address;
import com.phoenix.amazon.AmazonBackend.entity.Category;
import com.phoenix.amazon.AmazonBackend.entity.PassWordSet;
import com.phoenix.amazon.AmazonBackend.entity.Users;
import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.CATEGORY_FIELDS;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELD_VALIDATION.VALIDATE_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_VALIDATION.GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL;


public abstract class AbstractService extends AbstractValidationService {
    private final IUserRepository userRepository;
    private final IUserValidationService userValidationService;

    protected AbstractService(final IUserRepository userRepository, final IUserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
    }

    protected Users loadUserByUserIdOrUserNameOrPrimaryEmail(final String userId, final String userName, final String primaryEmail, final String methodName) throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
        // validate all non null PZeroFields
        userValidationService.validatePZeroUserFields(userId, userName, primaryEmail, methodName, VALIDATE_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL);
        Optional<Users> users = userRepository.findByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);
        // validate users exist or not
        userValidationService.validateUser(Optional.empty(), users, methodName, GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL);
        return users.get();
    }

    protected StringBuffer getUserDbField(USER_FIELDS sortBy) {
        StringBuffer sortByColumn = new StringBuffer();
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

    protected Category constructCategory(final Category oldCategory, final Category newCategory, final CATEGORY_FIELDS categoryFields) {
        switch (categoryFields) {
            case TITLE -> {
                return new Category.builder()
                        .title(newCategory.getTitle())
                        .categoryId(oldCategory.getCategoryId())
                        .description(oldCategory.getDescription())
                        .coverImage(oldCategory.getCoverImage())
                        .build();
            }
            case DESCRIPTION -> {
                return new Category.builder()
                        .description(newCategory.getDescription())
                        .categoryId(oldCategory.getCategoryId())
                        .title(oldCategory.getTitle())
                        .coverImage(oldCategory.getCoverImage())
                        .build();
            }
            case COVER_IMAGE -> {
                return new Category.builder()
                        .coverImage(newCategory.getCoverImage())
                        .title(oldCategory.getTitle())
                        .description(oldCategory.getDescription()).build();
            }
        }
        return oldCategory;
    }
    /**
     * no setter in entity class to stop partial initialization
     * so we need constructUser
     **/
    /**
     * @param oldUser - old user object
     * @param newUser - new user object
     * @param fields  - field of user entity
     * @return Users
     **/
    protected Users constructUser(final Users oldUser, final Users newUser, final USER_FIELDS fields) {
        switch (fields) {
            case USER_NAME -> {
                return new Users.builder()
                        .userName(newUser.getUserName())
                        .userId(oldUser.getUserId())
                        .firstName(oldUser.getFirstName())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .about(oldUser.getAbout())
                        .profileImage(oldUser.getProfileImage())
                        .password(oldUser.getPassword())
                        .previous_password_set(oldUser.getPrevious_password_set())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case FIRST_NAME -> {
                return new Users.builder()
                        .firstName(newUser.getFirstName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .about(oldUser.getAbout())
                        .password(oldUser.getPassword())
                        .previous_password_set(oldUser.getPrevious_password_set())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case LAST_NAME -> {
                return new Users.builder()
                        .lastName(newUser.getLastName())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .firstName(oldUser.getFirstName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .about(oldUser.getAbout())
                        .password(oldUser.getPassword())
                        .previous_password_set(oldUser.getPrevious_password_set())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();

            }
            case ABOUT -> {
                return new Users.builder()
                        .about(newUser.getAbout())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .firstName(oldUser.getFirstName())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .password(oldUser.getPassword())
                        .previous_password_set(oldUser.getPrevious_password_set())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();

            }
            case PRIMARY_EMAIL -> {
                return new Users.builder()
                        .primaryEmail(newUser.getPrimaryEmail())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .firstName(oldUser.getFirstName())
                        .lastName(oldUser.getLastName())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .about(oldUser.getAbout())
                        .password(oldUser.getPassword())
                        .previous_password_set(oldUser.getPrevious_password_set())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case SECONDARY_EMAIL -> {
                return new Users.builder()
                        .secondaryEmail(newUser.getSecondaryEmail())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .firstName(oldUser.getFirstName())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .gender(oldUser.getGender())
                        .about(oldUser.getAbout())
                        .password(oldUser.getPassword())
                        .profileImage(oldUser.getProfileImage())
                        .previous_password_set(oldUser.getPrevious_password_set())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case GENDER -> {
                return new Users.builder()
                        .gender(newUser.getGender())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .firstName(oldUser.getFirstName())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .about(oldUser.getAbout())
                        .password(oldUser.getPassword())
                        .previous_password_set(oldUser.getPrevious_password_set())
                        .profileImage(oldUser.getProfileImage())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case PASSWORD -> {
                Set<PassWordSet> oldPassWordSet = oldUser.getPrevious_password_set();
                if (CollectionUtils.isEmpty(oldPassWordSet)) oldPassWordSet = new HashSet<>();
                PassWordSet newPassWordSet = new PassWordSet.builder()
                        .password_id(UUID.randomUUID().toString())
                        .passwords(newUser.getPassword())
                        .users(oldUser).build();
                oldPassWordSet.add(newPassWordSet);
                return new Users.builder()
                        .password(newUser.getPassword())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .firstName(oldUser.getFirstName())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .about(oldUser.getAbout())
                        .profileImage(oldUser.getProfileImage())
                        .previous_password_set(oldPassWordSet)
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case PROFILE_IMAGE -> {
                return new Users.builder()
                        .profileImage(newUser.getProfileImage())
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .firstName(oldUser.getFirstName())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .about(oldUser.getAbout())
                        .password(oldUser.getPassword())
                        .previous_password_set(oldUser.getPrevious_password_set())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }
            case SET_ADDRESS -> {
                Set<Address> oldAddressSet = oldUser.getAddress_set();
                if(CollectionUtils.isEmpty(oldAddressSet)) oldAddressSet=new HashSet<>();
                oldAddressSet.addAll(newUser.getAddress_set());
                return new Users.builder()
                        .address_set(oldAddressSet)
                        .userId(oldUser.getUserId())
                        .userName(oldUser.getUserName())
                        .firstName(oldUser.getFirstName())
                        .lastName(oldUser.getLastName())
                        .primaryEmail(oldUser.getPrimaryEmail())
                        .secondaryEmail(oldUser.getSecondaryEmail())
                        .gender(oldUser.getGender())
                        .profileImage(oldUser.getProfileImage())
                        .about(oldUser.getAbout())
                        .password(oldUser.getPassword())
                        .previous_password_set(oldUser.getPrevious_password_set())
                        .lastSeen(oldUser.getLastSeen())
                        .build();
            }

        }
        return oldUser;
    }
}
