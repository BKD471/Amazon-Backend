//package com.phoenix.amazon.AmazonBackend.services.impls;
//
//import com.phoenix.amazon.AmazonBackend.dto.responseDtos.PageableResponse;
//import com.phoenix.amazon.AmazonBackend.dto.requestDtos.PasswordUpdateDto;
//import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UpdateUserDto;
//import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UserDto;
//import com.phoenix.amazon.AmazonBackend.entity.Users;
//import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
//import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
//import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
//import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers;
//import com.phoenix.amazon.AmazonBackend.repository.IUserRepository;
//import com.phoenix.amazon.AmazonBackend.services.IUserService;
//import com.phoenix.amazon.AmazonBackend.services.validationservice.IUserValidationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.Set;
//
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.NON_BINARY;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.FEMALE;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;
//
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.FIRST_NAME;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.GENDER;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.LAST_NAME;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.PRIMARY_EMAIL;
//import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.USER_FIELDS.USER_NAME;
//import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserToUpdateUserDto;
//import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UserUpdateDtoToUsers;
//import static com.phoenix.amazon.AmazonBackend.helpers.MappingHelpers.UsersToUsersDto;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyCollection;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.times;
//
//
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//public class UserServiceImplTest {
//    private final String TEST_PRIMARY_EMAIL = "test@gmail.com";
//    private final String TEST_SECONDARY_EMAIL = "tests@gmail.com";
//    private final String TEST_USER_NAME = "TEST_USER_NAME";
//    private final String TEST_UUID = "cac3092e-393d-4e42-a16b-5c3e76e4a57c";
//    private final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
//    private final String TEST_LAST_NAME = "TEST_LAST_NAME";
//    private final AllConstantHelpers.GENDER TEST_GENDER = MALE;
//    private final String TEST_PASSWORD = "TEST_PASSWORD";
//    private final String TEST_PROFILE_IMAGE = "cac3092e-393d-4e42-a16b-5c3e76e4a57c.jpg";
//    private final String TEST_ABOUT = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
//            "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
//            "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
//            "optio, eaque rerum! Provident similique accusantium nemo autem. Veritatis\n" +
//            "obcaecati tenetur iure eius earum ut molestias architecto voluptate aliquam\n" +
//            "nihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit,\n";
//    private final LocalDateTime TEST_LAST_SEEN = LocalDateTime.now();
//    private final String ADMIN = "ADMIN";
//    private final int TEST_PAGE_NUMBER = 1;
//    private final int TEST_PAGE_SIZE = 10;
//    private final String TEST_SORT_BY = "firstName";
//    private final String TEST_SORT_DIRECTION = "asc";
//
//    @Mock
//    private IUserValidationService userValidationServiceMock;
//    @Mock
//    private IUserRepository userRepositoryMock;
//
//    @Value("${path.services.image.properties}")
//    private String PATH_TO_IMAGE_PROPS;
//
//    @MockBean
//    private IUserService userServiceMock;
//
//    @BeforeEach
//    public void setUp() {
//        userServiceMock = new UserServiceImpl(userRepositoryMock, userValidationServiceMock, PATH_TO_IMAGE_PROPS);
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- createUser() With valid fields")
//    public void testCreateUserServiceHappyPath() throws UserExceptions, UserNotFoundExceptions, BadApiRequestExceptions, IOException {
//        // When
//        when(userRepositoryMock.save(any())).thenReturn(constructUser());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        UserDto userDto = userServiceMock.createUserService(UsersToUsersDto(constructUser()));
//
//        // Then
//        assertThat(userDto.userName()).isEqualTo(TEST_USER_NAME);
//        assertThat(userDto.primaryEmail()).isEqualTo(TEST_PRIMARY_EMAIL);
//        assertThat(userDto.userId()).isEqualTo(TEST_UUID);
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- createUserService() with Null User")
//    public void testCreateUserUnhappyPathNullUser() throws  BadApiRequestExceptions {
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> {
//            userServiceMock.createUserService(null);
//        }, "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- createUserService() with null UserName")
//    public void testCreateUserServiceUnhappyPathNullUserName() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        UserDto userRequest = new UserDto.builder()
//                .userId(TEST_UUID)
//                .userName(null)
//                .firstName(TEST_FIRST_NAME)
//                .lastName(TEST_LAST_NAME)
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .secondaryEmail(TEST_SECONDARY_EMAIL)
//                .about(TEST_ABOUT)
//                .gender(TEST_GENDER.toString())
//                .password(TEST_PASSWORD)
//                .profileImage(TEST_PROFILE_IMAGE)
//                .lastSeen(TEST_LAST_SEEN)
//                .build();
//        // When
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
//                "Null UserName prohibited"
//                , "testCreateUserUnhappyPathNullUserName"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> {
//            userServiceMock.createUserService(userRequest);
//        }, "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- createUserService() with null PrimaryEmail")
//    public void testCreateUserServiceUnhappyPathNullEmail() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        UserDto userRequest = new UserDto.builder()
//                .userId(TEST_UUID)
//                .userName(TEST_USER_NAME)
//                .firstName(TEST_FIRST_NAME)
//                .lastName(TEST_LAST_NAME)
//                .primaryEmail(null)
//                .about(TEST_ABOUT)
//                .gender(TEST_GENDER.toString())
//                .password(TEST_PASSWORD)
//                .profileImage(TEST_PROFILE_IMAGE)
//                .lastSeen(TEST_LAST_SEEN)
//                .build();
//        // When
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
//                "Null Email prohibited"
//                , "testCreateUserUnhappyPathNullEmail"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> {
//            userServiceMock.createUserService(userRequest);
//        }, "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- createUserService() with null FirstName")
//    public void testCreateUserServiceUnhappyPathNullFirstName() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        UserDto userRequest = new UserDto.builder()
//                .userId(TEST_UUID)
//                .userName(TEST_USER_NAME)
//                .firstName(null)
//                .lastName(TEST_LAST_NAME)
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .secondaryEmail(TEST_SECONDARY_EMAIL)
//                .about(TEST_ABOUT)
//                .gender(TEST_GENDER.toString())
//                .password(TEST_PASSWORD)
//                .profileImage(TEST_PROFILE_IMAGE)
//                .lastSeen(TEST_LAST_SEEN)
//                .build();
//        // When
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
//                "Null FirstName prohibited"
//                , "testCreateUserUnhappyPathNullFirstName"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> {
//            userServiceMock.createUserService(userRequest);
//        }, "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- createUserService() with null LastName")
//    public void testCreateUserServiceUnhappyPathNullLastName() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        UserDto userRequest = new UserDto.builder()
//                .userId(TEST_UUID)
//                .userName(TEST_USER_NAME)
//                .firstName(TEST_FIRST_NAME)
//                .lastName(null)
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .secondaryEmail(TEST_SECONDARY_EMAIL)
//                .about(TEST_ABOUT)
//                .gender(TEST_GENDER.toString())
//                .password(TEST_PASSWORD)
//                .profileImage(TEST_PROFILE_IMAGE)
//                .lastSeen(TEST_LAST_SEEN)
//                .build();
//        // When
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
//                "Null LastName prohibited"
//                , "testCreateUserUnhappyPathNullFirstName"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> {
//            userServiceMock.createUserService(userRequest);
//        }, "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- createUserService() with null Gender")
//    public void testCreateUserServiceUnhappyPathNullGender() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        UserDto userRequest = new UserDto.builder()
//                .userId(TEST_UUID)
//                .userName(TEST_USER_NAME)
//                .firstName(TEST_FIRST_NAME)
//                .lastName(TEST_LAST_NAME)
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .secondaryEmail(TEST_SECONDARY_EMAIL)
//                .about(TEST_ABOUT)
//                .gender(null)
//                .password(TEST_PASSWORD)
//                .profileImage(TEST_PROFILE_IMAGE)
//                .lastSeen(TEST_LAST_SEEN)
//                .build();
//        // When
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
//                "Null Gender prohibited"
//                , "testCreateUserUnhappyPathNullFirstName"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> {
//            userServiceMock.createUserService(userRequest);
//        }, "BadApiRequestExceptions should have been thrown");
//    }
//
//
//    @Test
//    @DisplayName("Test Unhappy Path -- createUserService() with Primary email already existing")
//    public void testCreateUserServiceUnhappyPathExistingPrimaryEmail() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        // Given
//        UserDto userRequest = new UserDto.builder()
//                .userId(TEST_UUID)
//                .userName(TEST_USER_NAME)
//                .firstName(TEST_FIRST_NAME)
//                .lastName(TEST_LAST_NAME)
//                .primaryEmail("EXISTING_PRIMARY_EMAIL")
//                .secondaryEmail(TEST_SECONDARY_EMAIL)
//                .about(TEST_ABOUT)
//                .gender(TEST_GENDER.toString())
//                .password(TEST_PASSWORD)
//                .profileImage(TEST_PROFILE_IMAGE)
//                .lastSeen(TEST_LAST_SEEN)
//                .build();
//
//        // When
//        doThrow(new UserExceptions(UserExceptions.class, String.format("There's an account with Primary Email %s", userRequest.primaryEmail())
//                , "testCreateUserUnhappyPathExistingPrimaryEmail"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> {
//            userServiceMock.createUserService(userRequest);
//        }, "UserException should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- createUserService() with Secondary email already existing")
//    public void testCreateUserServiceUnhappyPathExistingSecondaryEmail() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        // Given
//        UserDto userRequest = new UserDto.builder()
//                .userId(TEST_UUID)
//                .userName(TEST_USER_NAME)
//                .firstName(TEST_FIRST_NAME)
//                .lastName(TEST_LAST_NAME)
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .secondaryEmail("EXISTING_SECONDARY_EMAIL")
//                .about(TEST_ABOUT)
//                .gender(TEST_GENDER.toString())
//                .password(TEST_PASSWORD)
//                .profileImage(TEST_PROFILE_IMAGE)
//                .lastSeen(TEST_LAST_SEEN)
//                .build();
//
//        // When
//        doThrow(new UserExceptions(UserExceptions.class, String.format("There's an account with Secondary Email %s", userRequest.primaryEmail())
//                , "testCreateUserUnhappyPathExistingPrimaryEmail"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> {
//            userServiceMock.createUserService(userRequest);
//        }, "UserException should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- createUserService() with existing UserName")
//    public void testCreateUserServiceUnhappyPathNullWithExistingUserName() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        Users users = constructUser();
//        UserDto userRequest = new UserDto.builder()
//                .userId(users.getUserId())
//                .userName("EXISTING_USER_NAME")
//                .firstName(users.getFirstName())
//                .lastName(users.getLastName())
//                .primaryEmail(users.getPrimaryEmail())
//                .secondaryEmail(users.getSecondaryEmail())
//                .about(users.getAbout())
//                .gender(users.getGender().toString())
//                .password(users.getPassword())
//                .profileImage(users.getProfileImage())
//                .lastSeen(users.getLastSeen())
//                .build();
//        // When
//        doThrow(new UserExceptions(UserExceptions.class,
//                String.format("There's an account with UserName %s", userRequest.userName())
//                , "testCreateUserUnhappyPathNullWithExistingUserName"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> {
//            userServiceMock.createUserService(userRequest);
//        }, "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- createUserService() with existing UserId")
//    public void testCreateUserServiceUnhappyPathWithExistingUserId() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        Users users = constructUser();
//        UserDto userRequest = new UserDto.builder()
//                .userId("EXISTING_USER_ID")
//                .userName(users.getUserName())
//                .firstName(users.getFirstName())
//                .lastName(users.getLastName())
//                .primaryEmail(users.getPrimaryEmail())
//                .secondaryEmail(users.getSecondaryEmail())
//                .about(users.getAbout())
//                .gender(users.getGender().toString())
//                .password(users.getPassword())
//                .profileImage(users.getProfileImage())
//                .lastSeen(users.getLastSeen())
//                .build();
//        // When
//        doThrow(new UserExceptions(UserExceptions.class,
//                String.format("There's an account with UserId %s", userRequest.userId())
//                , "testCreateUserUnhappyPathNullWithExistingUserId"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> {
//            userServiceMock.createUserService(userRequest);
//        }, "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With all fields (firstName,lastName,Email,Gender,About) updated ")
//    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailAllFields() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        // Given
//        UpdateUserDto incomingUpdateUserDtoRequest = constructIncomingUpdateUserDtoRequest();
//
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(constructUser()));
//        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(constructIncomingUpdateUserDtoRequest()));
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(incomingUpdateUserDtoRequest, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//
//        // Then
//        assertThat(updatedUser.userName()).isEqualTo(incomingUpdateUserDtoRequest.userName());
//        assertThat(updatedUser.firstName()).isEqualTo(incomingUpdateUserDtoRequest.firstName());
//        assertThat(updatedUser.lastName()).isEqualTo(incomingUpdateUserDtoRequest.lastName());
//        assertThat(updatedUser.primaryEmail()).isEqualTo(incomingUpdateUserDtoRequest.primaryEmail());
//        assertThat(updatedUser.secondaryEmail()).isEqualTo(incomingUpdateUserDtoRequest.secondaryEmail());
//        assertThat(updatedUser.gender()).isEqualTo(incomingUpdateUserDtoRequest.gender());
//        assertThat(updatedUser.about()).isEqualTo(incomingUpdateUserDtoRequest.about());
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only userName updated")
//    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyUserNameChanged() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        // Given
//        final String UPDATED_USER_NAME = "UPDATED_USER_NAME";
//        Users users = constructUser();
//        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
//                .userName(UPDATED_USER_NAME)
//                .userId(users.getUserId())
//                .firstName(users.getFirstName())
//                .lastName(users.getLastName())
//                .primaryEmail(users.getPrimaryEmail())
//                .secondaryEmail(users.getSecondaryEmail())
//                .gender(users.getGender().toString())
//                .about(users.getAbout())
//                .build();
//
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
//        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//
//        // Then
//        assertThat(updatedUser.userName()).isEqualTo(UPDATED_USER_NAME);
//        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
//        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
//        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
//        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
//        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
//        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only primary email updated")
//    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyPrimaryEmailChanged() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        // Given
//        final String UPDATED_PRIMARY_EMAIL = "UPDATED_PRIMARY_EMAIL";
//        Users users = constructUser();
//        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
//                .userId(users.getUserId())
//                .userName(users.getUserName())
//                .firstName(users.getFirstName())
//                .lastName(users.getLastName())
//                .primaryEmail(UPDATED_PRIMARY_EMAIL)
//                .secondaryEmail(users.getSecondaryEmail())
//                .gender(users.getGender().toString())
//                .about(users.getAbout())
//                .build();
//
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
//        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//
//        // Then
//        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
//        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
//        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
//        assertThat(updatedUser.primaryEmail()).isEqualTo(UPDATED_PRIMARY_EMAIL);
//        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
//        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
//        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only secondary email updated")
//    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlySecondaryEmailChanged() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        // Given
//        final String UPDATED_SECONDARY_EMAIL = "UPDATED_SECONDARY_EMAIL";
//        Users users = constructUser();
//        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
//                .userId(users.getUserId())
//                .userName(users.getUserName())
//                .firstName(users.getFirstName())
//                .lastName(users.getLastName())
//                .primaryEmail(users.getPrimaryEmail())
//                .secondaryEmail(UPDATED_SECONDARY_EMAIL)
//                .gender(users.getGender().toString())
//                .about(users.getAbout())
//                .build();
//
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
//        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//
//        // Then
//        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
//        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
//        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
//        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
//        assertThat(updatedUser.secondaryEmail()).isEqualTo(UPDATED_SECONDARY_EMAIL);
//        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
//        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only firstName updated")
//    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyFirstNameChanged() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        // Given
//        final String UPDATED_FIRST_NAME = "UPDATED_FIRST_NAME";
//        Users users = constructUser();
//        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
//                .userId(users.getUserId())
//                .userName(users.getUserName())
//                .firstName(UPDATED_FIRST_NAME)
//                .lastName(users.getLastName())
//                .primaryEmail(users.getPrimaryEmail())
//                .secondaryEmail(users.getSecondaryEmail())
//                .gender(users.getGender().toString())
//                .about(users.getAbout())
//                .build();
//
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
//        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//
//        // Then
//        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
//        assertThat(updatedUser.firstName()).isEqualTo(UPDATED_FIRST_NAME);
//        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
//        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
//        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
//        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
//        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only lastName updated")
//    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyLastNameChanged() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        final String UPDATED_LAST_NAME = "UPDATED_LAST_NAME";
//        Users users = constructUser();
//        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
//                .userId(users.getUserId())
//                .userName(users.getUserName())
//                .firstName(users.getFirstName())
//                .lastName(UPDATED_LAST_NAME)
//                .primaryEmail(users.getPrimaryEmail())
//                .secondaryEmail(users.getSecondaryEmail())
//                .gender(users.getGender().toString())
//                .about(users.getAbout())
//                .build();
//
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
//        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//
//        // Then
//        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
//        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
//        assertThat(updatedUser.lastName()).isEqualTo(UPDATED_LAST_NAME);
//        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
//        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
//        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
//        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
//    }
//
//
//    @Test
//    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only gender updated")
//    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyGenderChanged() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        final String UPDATED_GENDER = "FEMALE";
//        Users users = constructUser();
//        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
//                .userId(users.getUserId())
//                .userName(users.getUserName())
//                .firstName(users.getFirstName())
//                .lastName(users.getLastName())
//                .primaryEmail(users.getPrimaryEmail())
//                .secondaryEmail(users.getSecondaryEmail())
//                .gender(UPDATED_GENDER)
//                .about(users.getAbout())
//                .build();
//
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
//        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//
//        // Then
//        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
//        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
//        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
//        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
//        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
//        assertThat(updatedUser.about()).isEqualTo(users.getAbout());
//        assertThat(updatedUser.gender()).isEqualTo(UPDATED_GENDER);
//    }
//
//
//    @Test
//    @DisplayName("Test Happy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With only about updated")
//    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailWithOnlyAboutChanged() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        // Given
//        final String UPDATED_ABOUT = "UPDATED_ABOUT";
//        Users users = constructUser();
//        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
//                .userId(users.getUserId())
//                .userName(users.getUserName())
//                .firstName(users.getFirstName())
//                .lastName(users.getLastName())
//                .primaryEmail(users.getPrimaryEmail())
//                .secondaryEmail(users.getSecondaryEmail())
//                .gender(users.getGender().toString())
//                .about(UPDATED_ABOUT)
//                .build();
//
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL)).thenReturn(Optional.of(users));
//        when(userRepositoryMock.save(any())).thenReturn(UserUpdateDtoToUsers(updatedUpdateUserDto));
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        UserDto updatedUser = userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//
//        // Then
//        assertThat(updatedUser.userName()).isEqualTo(users.getUserName());
//        assertThat(updatedUser.firstName()).isEqualTo(users.getFirstName());
//        assertThat(updatedUser.lastName()).isEqualTo(users.getLastName());
//        assertThat(updatedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
//        assertThat(updatedUser.secondaryEmail()).isEqualTo(users.getSecondaryEmail());
//        assertThat(updatedUser.about()).isEqualTo(UPDATED_ABOUT);
//        assertThat(updatedUser.gender()).isEqualTo(users.getGender().toString());
//    }
//
//
//    @Test
//    @DisplayName("Test Unhappy Path -- updateUserByUserIdOrUserName() With both UserId & UserName Null")
//    public void testUpdateUserByUserIdOrUserNameUnhappyPathWithBothUserIdAndUserNameAndPrimaryEmailIsNull() throws BadApiRequestExceptions {
//        // Given
//        Users users = constructUser();
//
//        // When
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class, "Please provide non null username or user Id", "testUpdateUserByUserIdOrUserNameUnhappyPathWithBothUserIdAndUserNameNull"))
//                .when(userValidationServiceMock).validatePZeroUserFields(any(), any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> {
//            userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(UserToUpdateUserDto(users), null, null, null);
//        }, "BadApiRequestException should have been thrown");
//    }
//
//
//    @Test
//    @DisplayName("Test Unhappy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With UserId & UserName & Primary Email invalid or not present in db")
//    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailUnhappyPathForUserNotFound() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        Users users = constructUser();
//
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail("INVALID_USER_ID",
//                "INVALID_USER_NAME", "INVALID_PRIMARY_EMAIL")).thenReturn(Optional.empty());
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class, "No User with this UserId or UserName"
//                , "testUpdateUserByUserIdOrUserNameUnhappyPathForUserNotFound"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(UserToUpdateUserDto(users),
//                    "INVALID_USER_ID", "INVALID_USER_NAME", "INVALID_PRIMARY_EMAIL");
//        }, "UserNotFoundException should have been thrown");
//    }
//
//
//    @Test
//    @DisplayName("Test Unhappy Path -- updateUserServiceByUserIdOrUserNameOrPrimaryEmail() With primary email already existing")
//    public void testUpdateUserServiceByUserIdOrUserNameOrPrimaryEmailUnhappyPathWithExistingEmail() throws UserNotFoundExceptions, UserExceptions, BadApiRequestExceptions, IOException {
//        // Given
//        Users users = constructUser();
//        UpdateUserDto updatedUpdateUserDto = new UpdateUserDto.builder()
//                .userId(users.getUserId())
//                .userName(users.getUserName())
//                .firstName(users.getFirstName())
//                .lastName(users.getLastName())
//                .primaryEmail("EXISTING_PRIMARY_EMAIL")
//                .secondaryEmail(users.getSecondaryEmail())
//                .gender(users.getGender().toString())
//                .about(users.getAbout())
//                .build();
//
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL))
//                .thenReturn(Optional.of(users));
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doNothing().doThrow(new UserExceptions(UserExceptions.class,
//                        String.format("There's an account with Email %s EMAIL", updatedUpdateUserDto.primaryEmail())
//                        , "testUpdateUserByUserIdOrUserNameUnhappyPathWithExistingEmail"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(UserExceptions.class, () -> {
//            userServiceMock.updateUserServiceByUserIdOrUserNameOrPrimaryEmail(updatedUpdateUserDto, TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//        }, "UserExceptions Should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- deleteUserServiceByUserIdOrUserNameOrPrimaryEmail() With valid UserId & UserName & primary Email")
//    public void testDeleteByUserIdOrUserNameOrPrimaryEmailHappyPath() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        Users users = constructUser();
//
//        // When
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL))
//                .thenReturn(Optional.of(users)).thenReturn(Optional.empty());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        userServiceMock.deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//        Optional<Users> fetchedUser = userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//
//        // Then
//        verify(userRepositoryMock, times(1)).deleteByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//        assertThat(fetchedUser.isEmpty()).isTrue();
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- deleteUserServiceByUserIdOrUserNameOrPrimaryEmail() With UserId Only")
//    public void testDeleteByUserIdOrUserNameOrPrimaryEmailHappyPathWithUserIdOnly() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        Users users = constructUser();
//
//        // When
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), any(), any(), anyString(), any());
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, null, null))
//                .thenReturn(Optional.of(users)).thenReturn(Optional.empty());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        userServiceMock.deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, null, null);
//        Optional<Users> fetchedUser = userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, null, null);
//
//        // Then
//        verify(userRepositoryMock, times(1)).deleteByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, null, null);
//        assertThat(fetchedUser.isEmpty()).isTrue();
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- deleteUserServiceByUserIdOrUserNameOrPrimaryEmail() With UserName Only")
//    public void testDeleteByUserIdOrUserNameOrPrimaryEmailHappyPathWithUserNameOnly() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        Users users = constructUser();
//
//        // When
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(any(), anyString(), any(), anyString(), any());
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(null, TEST_USER_NAME, null))
//                .thenReturn(Optional.of(users)).thenReturn(Optional.empty());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        userServiceMock.deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(null, TEST_USER_NAME, null);
//        Optional<Users> fetchedUser = userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(null, TEST_USER_NAME, null);
//
//        // Then
//        verify(userRepositoryMock, times(1)).deleteByUserIdOrUserNameOrPrimaryEmail(null, TEST_USER_NAME, null);
//        assertThat(fetchedUser.isEmpty()).isTrue();
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- deleteUserServiceByUserIdOrUserNameOrPrimaryEmail() With PrimaryEmail Only")
//    public void testDeleteByUserIdOrUserNameOrPrimaryEmailHappyPathWithPrimaryEmailOnly() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        Users users = constructUser();
//
//        // When
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(any(), any(), anyString(), anyString(), any());
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(null, null, TEST_PRIMARY_EMAIL))
//                .thenReturn(Optional.of(users)).thenReturn(Optional.empty());
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        userServiceMock.deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(null, null, TEST_PRIMARY_EMAIL);
//        Optional<Users> fetchedUser = userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(null, null, TEST_PRIMARY_EMAIL);
//
//        // Then
//        verify(userRepositoryMock, times(1)).deleteByUserIdOrUserNameOrPrimaryEmail(null, null, TEST_PRIMARY_EMAIL);
//        assertThat(fetchedUser.isEmpty()).isTrue();
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- deleteUserServiceByUserIdOrUserNameOrPrimaryEmail() With null UserId & UserName & primaryEmail")
//    public void testDeleteUserServiceByUserIdOrUserNameOrPrimaryEmailUnhappyPathForNullUserIdAndUserName() throws BadApiRequestExceptions {
//        // When
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class, "Please provide non null username or user Id or primary Email",
//                "testDeleteUserServiceByUserIdOrUserNameOrPrimaryEmailUnhappyPathForNullUserIdAndUserName()"))
//                .when(userValidationServiceMock).validatePZeroUserFields(any(), any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(BadApiRequestExceptions.class, () -> {
//            userServiceMock.deleteUserServiceByUserIdOrUserNameOrPrimaryEmail(null, null, null);
//        }, "BadApiRequestException should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- deleteUserServiceByUserIdOrUserNameOrPrimaryEmail() With InValid UserId & UserName & Primary Email")
//    public void testDeleteUserServiceByUserIdOrUserNameOrPrimaryEmailUnhappyPathForInValidUserIdAndUserName() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // When
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail("INVALID_USER_ID", "INVALID_USER_NAME", "INVALID_PRIMARY_EMAIL")).thenReturn(Optional.empty());
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class, "No User with this UserId or UserName or primaryEmail",
//                "testDeleteUserByUserIdOrUserNameUnhappyPathForInValidUserIdAndUserName()"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.deleteUserServiceByUserIdOrUserNameOrPrimaryEmail("INVALID_USER_ID",
//                    "INVALID_USER_NAME", "INVALID_PRIMARY_EMAIL");
//        }, "UserNotFoundException should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- testGetALlUsersHappyPath() with Users Present")
//    public void testGetALlUsersHappyPath() throws UserNotFoundExceptions {
//        // When
//        when(userRepositoryMock.findAll(any(Pageable.class))).thenReturn(constructUserPage());
//        doNothing().when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//        PageableResponse<UserDto> userPages = userServiceMock.getAllUsers(TEST_PAGE_NUMBER, TEST_PAGE_SIZE,
//                TEST_SORT_BY, TEST_SORT_DIRECTION);
//
//        //Then
//        assertThat(userPages.getContent().isEmpty()).isFalse();
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- testGetAllUsersHappyPath() with No Users Present")
//    public void testGetALlUsersUnhappyPath() throws UserNotFoundExceptions {
//        // When
//        when(userRepositoryMock.findAll(any(Pageable.class))).thenReturn(Page.empty());
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users", "testGetALlUsersHappyPath"))
//                .when(userValidationServiceMock).validateUserList(any(), anyString(), any());
//
//        //Then
//        assertThrows(UserNotFoundExceptions.class,
//                () -> {
//                    userServiceMock.getAllUsers(TEST_PAGE_NUMBER, TEST_PAGE_SIZE, TEST_SORT_BY, TEST_SORT_DIRECTION);
//                },
//                "UserNotFound Exception Should Be thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail() With Valid UserId & UserName & Primary Email")
//    public void testGetUserServiceInformationByUserIdOrUserNameOrPrimaryEmailHappyPath() throws UserExceptions, BadApiRequestExceptions, UserNotFoundExceptions, IOException {
//        // Given
//        Users users = constructUser();
//
//        // When
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(), anyString(), anyString(), any());
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL))
//                .thenReturn(Optional.of(constructUser()));
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        UserDto fetchedUser = userServiceMock.getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(TEST_UUID, TEST_USER_NAME, TEST_PRIMARY_EMAIL);
//
//        // Then
//        assertThat(fetchedUser.userName()).isEqualTo(users.getUserName());
//        assertThat(fetchedUser.userId()).isEqualTo(users.getUserId());
//        assertThat(fetchedUser.primaryEmail()).isEqualTo(users.getPrimaryEmail());
//        assertThat(fetchedUser.firstName()).isEqualTo(users.getFirstName());
//        assertThat(fetchedUser.lastName()).isEqualTo(users.getLastName());
//        assertThat(fetchedUser.gender()).isEqualTo(users.getGender().toString());
//        assertThat(fetchedUser.about()).isEqualTo(users.getAbout());
//        assertThat(fetchedUser.profileImage()).isEqualTo(users.getProfileImage());
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail() With null userId & UserName & Primary Email")
//    public void testGetUserServiceInformationByUserIdOrUserNameOrPrimaryEmailUnhappyPathWithNullValues() throws BadApiRequestExceptions {
//        // When
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
//                "Please provide non null userId or username or email",
//                "testGetUserServiceInformationByUserIdOrUserNameOrPrimaryEmailUnhappyPathWithNullValues"))
//                .when(userValidationServiceMock).validatePZeroUserFields(any(), any(), any(), anyString(), any());
//
//        assertThrows(BadApiRequestExceptions.class,
//                () -> {
//                    userServiceMock.getUserServiceInformationByUserIdOrUserNameOrPrimaryEmail(null, null, null);
//                },
//                "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- searchAllUsersByUserName() with valid matching userName present in DB")
//    public void testSearchAllUsersByUserNameHappyPath() throws UserNotFoundExceptions {
//        // Given
//        Page<Users> usersPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.findAllByUserNameContaining(anyString(), any(Pageable.class)))
//                .thenReturn(Optional.of(usersPage));
//        doNothing().when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//        PageableResponse<UserDto> fetchedUserPage = userServiceMock.searchAllUsersByUserName(TEST_USER_NAME, TEST_PAGE_NUMBER,
//                TEST_PAGE_SIZE, TEST_SORT_BY, TEST_SORT_DIRECTION);
//
//        // Then
//        assertThat(fetchedUserPage.getContent().isEmpty()).isFalse();
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchAllUsersByUserName() with no matching userName present in DB")
//    public void testSearchAllUsersByUserNameUnhappyPath() throws UserNotFoundExceptions {
//        // Given
//        Page<Users> userPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.findAllByUserNameContaining(anyString(), any(Pageable.class)))
//                .thenReturn(Optional.of(userPage));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this UserName",
//                "testSearchAllUsersByUserNameUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchAllUsersByUserName("NOT_AVAILABLE_USER_NAME", TEST_PAGE_NUMBER,
//                    TEST_PAGE_SIZE, TEST_SORT_BY, TEST_SORT_DIRECTION);
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with valid email present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsEmail() throws UserNotFoundExceptions {
//        // Given
//        Page<Users> userPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.searchUserByEmail(anyString(), any(Pageable.class))).thenReturn(Optional.of(userPage));
//        doNothing().when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//        PageableResponse<UserDto> fetchedUserPage = userServiceMock.searchUserByFieldAndValue(PRIMARY_EMAIL,
//                TEST_PRIMARY_EMAIL, TEST_PAGE_NUMBER, TEST_PAGE_SIZE, PRIMARY_EMAIL, TEST_SORT_DIRECTION);
//
//        // Then
//        assertThat(fetchedUserPage.getContent().isEmpty()).isFalse();
//        assertThat(fetchedUserPage.getContent().stream().toList().getFirst().primaryEmail()).isEqualTo(TEST_PRIMARY_EMAIL);
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchUserByFieldAndValue() with email not present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsEmailUnhappyPath() throws UserNotFoundExceptions {
//        //Given
//        Page<Users> usersPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.searchUserByEmail(anyString(), any(Pageable.class)))
//                .thenReturn(Optional.of(usersPage));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this email",
//                "testSearchUserByFieldAndValueWhenFieldIsEmailUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchUserByFieldAndValue(PRIMARY_EMAIL,
//                    "INVALID_PRIMARY_EMAIL", TEST_PAGE_NUMBER, TEST_PAGE_SIZE, PRIMARY_EMAIL, TEST_SORT_DIRECTION);
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with valid userName present in DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsUserName() throws UserNotFoundExceptions {
//        // Given
//        Page<Users> usersPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.searchUserByUserName(anyString(), any(Pageable.class))).thenReturn(Optional.of(usersPage));
//        doNothing().when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//        PageableResponse<UserDto> fetchedUserPage = userServiceMock.searchUserByFieldAndValue(USER_NAME, TEST_USER_NAME,
//                TEST_PAGE_NUMBER, TEST_PAGE_SIZE, PRIMARY_EMAIL, TEST_SORT_DIRECTION);
//
//        // Then
//        assertThat(fetchedUserPage.getContent().isEmpty()).isFalse();
//        assertThat(fetchedUserPage.getContent().stream().toList().getFirst().userName()).isEqualTo(TEST_USER_NAME);
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchUserByFieldAndValue() with userName not present in DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsUserNameUnhappyPath() throws UserNotFoundExceptions {
//        //Given
//        Page<Users> usersPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.searchUserByUserName(anyString(), any(Pageable.class)))
//                .thenReturn(Optional.of(usersPage));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this email",
//                "testSearchUserByFieldAndValueWhenFieldIsEmailUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchUserByFieldAndValue(USER_NAME,
//                    "INVALID_USER_NAME", TEST_PAGE_NUMBER, TEST_PAGE_SIZE, PRIMARY_EMAIL, TEST_SORT_DIRECTION);
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with valid FirstName present in DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsFirstName() throws UserNotFoundExceptions {
//        // Given
//        Page<Users> usersPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.searchUserByFirstName(anyString(), any(Pageable.class))).thenReturn(Optional.of(usersPage));
//        doNothing().when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//        PageableResponse<UserDto> fetchedUserPages = userServiceMock.searchUserByFieldAndValue(FIRST_NAME, TEST_FIRST_NAME,
//                TEST_PAGE_NUMBER, TEST_PAGE_SIZE, PRIMARY_EMAIL, TEST_SORT_DIRECTION);
//
//        // Then
//        assertThat(fetchedUserPages.getContent().isEmpty()).isFalse();
//        assertThat(fetchedUserPages.getContent().stream().toList().getFirst().firstName()).isEqualTo(TEST_FIRST_NAME);
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchUserByFieldAndValue() with FirstName not present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsFirstNameUnhappyPath() throws UserNotFoundExceptions {
//        // Given
//        Page<Users> userPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.searchUserByFirstName(anyString(), any(Pageable.class))).thenReturn(Optional.of(userPage));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this firstName",
//                "testSearchUserByFieldAndValueWhenFieldIsFirstNameUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchUserByFieldAndValue(FIRST_NAME, "INVALID_FIRST_NAME",
//                    TEST_PAGE_NUMBER, TEST_PAGE_SIZE, PRIMARY_EMAIL, TEST_SORT_DIRECTION);
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with valid LastName present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsLastName() throws UserNotFoundExceptions {
//        // Given
//        Page<Users> usersPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.searchUserByLastName(anyString(), any(Pageable.class))).thenReturn(Optional.of(usersPage));
//        doNothing().when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//        PageableResponse<UserDto> fetchedUserPage = userServiceMock.searchUserByFieldAndValue(LAST_NAME, TEST_LAST_NAME,
//                TEST_PAGE_NUMBER, TEST_PAGE_SIZE, PRIMARY_EMAIL, TEST_SORT_DIRECTION);
//
//        // Then
//        assertThat(fetchedUserPage.getContent().isEmpty()).isFalse();
//        assertThat(fetchedUserPage.getContent().stream().toList().getFirst().lastName()).isEqualTo(TEST_LAST_NAME);
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchUserByFieldAndValue() with LastName not present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsLastNameUnhappyPath() throws UserNotFoundExceptions {
//        // Given
//        Page<Users> usersPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.searchUserByLastName(anyString(), any(Pageable.class))).thenReturn(Optional.of(usersPage));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this lastName",
//                "testSearchUserByFieldAndValueWhenFieldIsLastNameUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchUserByFieldAndValue(LAST_NAME, "INVALID_LAST_NAME", TEST_PAGE_NUMBER,
//                    TEST_PAGE_SIZE, PRIMARY_EMAIL, TEST_SORT_DIRECTION);
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//
//    @Test
//    @DisplayName("Test Happy Path -- searchUserByFieldAndValue() with valid gender present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsGender() throws UserNotFoundExceptions {
//        // Given
//        Page<Users> usersPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.searchUserByGender(anyString(), any(Pageable.class))).thenReturn(Optional.of(usersPage));
//        doNothing().when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//        PageableResponse<UserDto> fetchedUserPage = userServiceMock.searchUserByFieldAndValue(GENDER, TEST_GENDER.toString(),
//                TEST_PAGE_NUMBER,
//                TEST_PAGE_SIZE, PRIMARY_EMAIL, TEST_SORT_DIRECTION);
//
//        // Then
//        assertThat(fetchedUserPage.getContent().isEmpty()).isFalse();
//        assertThat(fetchedUserPage.getContent().stream().toList().getFirst().gender()).isEqualTo(TEST_GENDER.toString());
//    }
//
//    @Test
//    @DisplayName("Test Unhappy Path -- searchUserByFieldAndValue() with gender not present int DB")
//    public void testSearchUserByFieldAndValueWhenFieldIsGenderUnhappyPath() throws UserNotFoundExceptions {
//        // Given
//        Page<Users> usersPage = constructUserPage();
//
//        // When
//        when(userRepositoryMock.searchUserByGender(anyString(), any(Pageable.class))).thenReturn(Optional.of(usersPage));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "Our Database have no Users With this gender",
//                "testSearchUserByFieldAndValueWhenFieldIsGenderUnhappyPath"))
//                .when(userValidationServiceMock).validateUserList(anyCollection(), anyString(), any());
//
//        // Then
//        assertThrows(UserNotFoundExceptions.class, () -> {
//            userServiceMock.searchUserByFieldAndValue(GENDER, String.valueOf(FEMALE),
//                    TEST_PAGE_NUMBER,
//                    TEST_PAGE_SIZE, PRIMARY_EMAIL, TEST_SORT_DIRECTION);
//        }, "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test Generate Random Password")
//    public void testGeneratePasswordService() {
//        final String generatedPassword = userServiceMock.generatePasswordService();
//
//        boolean hasLowerCase = false, hasUpperCase = false, hasNumbers = false, hasSpecialCharacters = false;
//        for (int i = 0; i < generatedPassword.length(); i++) {
//            int ascii = generatedPassword.charAt(i);
//            if (ascii >= 65 && ascii <= 90) hasUpperCase = true;
//            if (ascii >= 97 && ascii <= 122) hasLowerCase = true;
//            if (ascii >= 48 && ascii <= 57) hasNumbers = true;
//            if ((ascii >= 33 && ascii <= 47) || (ascii>=60 && ascii<=64) || ascii==95 || ascii==124) hasSpecialCharacters = true;
//            if (hasLowerCase && hasUpperCase && hasNumbers && hasSpecialCharacters) break;
//        }
//
//        boolean result = generatedPassword.length() >= 16 && hasLowerCase && hasUpperCase && hasNumbers && hasSpecialCharacters;
//        assertThat(result).isTrue();
//    }
//
//
//    @Test
//    @DisplayName("Test Happy Path -- resetPasswordService()")
//    public void testResetPasswordService() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        final String NEW_PASSWORD = "NEW_PASSWORD";
//        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto.Builder()
//                .oldPassword(TEST_PASSWORD)
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .newPassword(NEW_PASSWORD)
//                .confirmPassword(NEW_PASSWORD)
//                .build();
//        Users resettedPasswordUser = new Users.builder()
//                .password(NEW_PASSWORD)
//                .build();
//
//
//        // When
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(),
//                anyString(), anyString(), any());
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_PRIMARY_EMAIL, TEST_PRIMARY_EMAIL, TEST_PRIMARY_EMAIL))
//                .thenReturn(Optional.of(resettedPasswordUser));
//        doNothing().when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//        userServiceMock.resetPasswordService(passwordUpdateDto);
//
//        // Then
//        Optional<Users> fetchedUser = userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail
//                (TEST_PRIMARY_EMAIL, TEST_PRIMARY_EMAIL, TEST_PRIMARY_EMAIL);
//        assertThat(fetchedUser.get().getPassword()).isEqualTo(NEW_PASSWORD);
//    }
//
//    @Test
//    @DisplayName("Test UnHappy Path -- resetPasswordService() when invalid primaryEmail not present in DB ")
//    public void testResetPasswordServiceUnhappyPathPrimaryEmailNotPresent() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        final String NEW_PASSWORD = "NEW_PASSWORD";
//        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto.Builder()
//                .oldPassword("I FORGOT MY OLD PASSWORD")
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .newPassword(NEW_PASSWORD)
//                .confirmPassword(NEW_PASSWORD)
//                .build();
//        Users resettedPasswordUser = new Users.builder()
//                .password(NEW_PASSWORD)
//                .build();
//
//
//        // When
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(),
//                anyString(), anyString(), any());
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_PRIMARY_EMAIL, TEST_PRIMARY_EMAIL, TEST_PRIMARY_EMAIL))
//                .thenReturn(Optional.of(resettedPasswordUser));
//        doThrow(new UserNotFoundExceptions(UserNotFoundExceptions.class,
//                "No Such Primary Email in DB",
//                "testResetPasswordServiceUnhappyPathPrimaryEmailNotPresent"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//
//        assertThrows(UserNotFoundExceptions.class, () -> userServiceMock.resetPasswordService(passwordUpdateDto),
//                "UserNotFoundExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test UnHappy Path -- resetPasswordService() when primaryEmail is null")
//    public void testResetPasswordServiceUnhappyPathPrimaryEmailNull() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        final String NEW_PASSWORD = "NEW_PASSWORD";
//        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto.Builder()
//                .oldPassword("I FORGOT MY OLD PASSWORD")
//                .primaryEmail(null)
//                .newPassword(NEW_PASSWORD)
//                .confirmPassword(NEW_PASSWORD)
//                .build();
//        Users resettedPasswordUser = new Users.builder()
//                .password(NEW_PASSWORD)
//                .build();
//
//
//        // When
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(any(), any(),
//                any(), anyString(), any());
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(any(), any(), any()))
//                .thenReturn(Optional.of(resettedPasswordUser));
//        doThrow(new BadApiRequestExceptions(BadApiRequestExceptions.class,
//                "Please provide non null primaryEmail",
//                "testResetPasswordServiceUnhappyPathPrimaryEmailNotPresent"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//
//        assertThrows(BadApiRequestExceptions.class, () -> userServiceMock.resetPasswordService(passwordUpdateDto),
//                "BadApiRequestExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test UnHappy Path -- resetPasswordService() when old password mismatched")
//    public void testResetPasswordServiceUnhappyPathOldPasswordNotMatched() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        final String NEW_PASSWORD = "NEW_PASSWORD";
//        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto.Builder()
//                .oldPassword("I FORGOT MY OLD PASSWORD")
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .newPassword(NEW_PASSWORD)
//                .confirmPassword(NEW_PASSWORD)
//                .build();
//        Users resettedPasswordUser = new Users.builder()
//                .password(NEW_PASSWORD)
//                .build();
//
//
//        // When
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(),
//                anyString(), anyString(), any());
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_PRIMARY_EMAIL, TEST_PRIMARY_EMAIL, TEST_PRIMARY_EMAIL))
//                .thenReturn(Optional.of(resettedPasswordUser));
//        doNothing().doThrow(new UserExceptions(UserExceptions.class,
//                        "Old Password not matched",
//                        "testResetPasswordServiceUnhappyPathOldPasswordNotMatched"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//
//        assertThrows(UserExceptions.class, () -> userServiceMock.resetPasswordService(passwordUpdateDto),
//                "UserExceptions should have been thrown");
//    }
//
//    @Test
//    @DisplayName("Test UnHappy Path -- resetPasswordService() when new & confirm password mismatched")
//    public void testResetPasswordServiceUnhappyPathNewAndConfirmPasswordMismatched() throws BadApiRequestExceptions, UserNotFoundExceptions, UserExceptions, IOException {
//        // Given
//        final String NEW_PASSWORD = "NEW_PASSWORD";
//        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto.Builder()
//                .oldPassword(TEST_PASSWORD)
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .newPassword(NEW_PASSWORD + "One")
//                .confirmPassword(NEW_PASSWORD + "TWO")
//                .build();
//        Users resettedPasswordUser = new Users.builder()
//                .password(NEW_PASSWORD)
//                .build();
//
//
//        // When
//        doNothing().when(userValidationServiceMock).validatePZeroUserFields(anyString(), anyString(),
//                anyString(), anyString(), any());
//        when(userRepositoryMock.findByUserIdOrUserNameOrPrimaryEmail(TEST_PRIMARY_EMAIL, TEST_PRIMARY_EMAIL, TEST_PRIMARY_EMAIL))
//                .thenReturn(Optional.of(resettedPasswordUser));
//        doNothing().doNothing().doThrow(new UserExceptions(UserExceptions.class,
//                        "New & Confirm Password mismatched",
//                        "testResetPasswordServiceUnhappyPathNewAndConfirmPasswordMismatched"))
//                .when(userValidationServiceMock).validateUser(any(), any(), anyString(), any());
//
//
//        assertThrows(UserExceptions.class, () -> userServiceMock.resetPasswordService(passwordUpdateDto),
//                "UserExceptions should have been thrown");
//    }
//
//
//    private Set<Users> constructUsersSet() {
//        return Set.of(constructUser(), constructUser());
//    }
//
//    private Page<Users> constructUserPage() {
//        return new PageImpl<>(constructUsersSet().stream().toList());
//    }
//
//    private UpdateUserDto constructIncomingUpdateUserDtoRequest() {
//        final String UPDATED_USER_NAME = "UPDATED_USER_NAME";
//        final String UPDATED_USER_ID = "144d02e1-49ab-417e-8279-ed08c997aed7";
//        final String UPDATED_FIRST_NAME = "UPDATED_FIRST_NAME";
//        final String UPDATED_LAST_NAME = "UPDATED_LAST_NAME";
//        final String UPDATED_PRIMARY_EMAIL = "UPDATED_PRIMARY_EMAIL";
//        final String UPDATED_SECONDARY_EMAIL = "UPDATED_SECONDARY_EMAIL";
//        final String UPDATED_ABOUT = "80000k ke jootey, ismein tera ghar chala jayenga";
//
//        return new UpdateUserDto.builder()
//                .userId(UPDATED_USER_ID)
//                .userName(UPDATED_USER_NAME)
//                .firstName(UPDATED_FIRST_NAME)
//                .lastName(UPDATED_LAST_NAME)
//                .primaryEmail(UPDATED_PRIMARY_EMAIL)
//                .secondaryEmail(UPDATED_SECONDARY_EMAIL)
//                .gender(NON_BINARY.toString())
//                .about(UPDATED_ABOUT)
//                .build();
//    }
//
//
//    private Users constructUser() {
//        return new Users.builder()
//                .userId(TEST_UUID)
//                .userName(TEST_USER_NAME)
//                .firstName(TEST_FIRST_NAME)
//                .lastName(TEST_LAST_NAME)
//                .primaryEmail(TEST_PRIMARY_EMAIL)
//                .secondaryEmail(TEST_SECONDARY_EMAIL)
//                .password(TEST_PASSWORD)
//                .gender(TEST_GENDER)
//                .profileImage(TEST_PROFILE_IMAGE)
//                .about(TEST_ABOUT)
//                .lastSeen(TEST_LAST_SEEN)
//                .createdDate(LocalDate.now().minusDays(10))
//                .createdBy(ADMIN)
//                .build();
//    }
//}
