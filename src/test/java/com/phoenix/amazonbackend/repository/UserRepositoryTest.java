package com.phoenix.amazonbackend.repository;

import com.phoenix.amazonbackend.entities.Users;
import com.phoenix.amazonbackend.utils.AllConstants;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.phoenix.amazonbackend.utils.AllConstants.GENDER.MALE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    @Test
    @Order(1)
    void create() {
        // Given
        final Users userData = constructUserRepositoryData();

        // When
        final Users savedUserData=userRepository.save(userData);

        // Then
        assertThat(userRepository.findAll()).containsOnly(savedUserData);
    }

    @Test
    @Order(2)
    void delete() {
         // When
        userRepository.deleteAll();

        // Then
        assertThat(userRepository.findAll()).isEmpty();
    }

    private Users constructUserRepositoryData() {
        final String TEST_PRIMARY_EMAIL = "testprimary@gmail.com";
        final String TEST_SECONDARY_EMAIL = "testsecondary@gmail.com";
        final String TEST_USER_NAME = "TEST_USER_NAME";
        final String TEST_UUID = "58824409-dd6b-4934-9923-ec1daf9693da";
        final String TEST_FIRST_NAME = "TEST_FIRST_NAME";
        final String TEST_LAST_NAME = "TEST_LAST_NAME";
        final AllConstants.GENDER TEST_GENDER = MALE;
        final String TEST_PASSWORD = "TEST_PASSWORD";
        final String TEST_PROFILE_IMAGE = "0ecbe17e-5537-4533-9b9f-b3c2438e58eb.jpg";
        final String TEST_ABOUT = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n" +
                "numquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentium\n" +
                "optio, eaque rerum! Provident similique accusantium nemo autem. Veritatis\n" +
                "obcaecati tenetur iure eius earum ut molestias architecto voluptate aliquam\n" +
                "nihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit,\n";
        final String ADMIN = "ADMIN";

        return  Users.builder()
                .userId(UUID.fromString(TEST_UUID))
                .userName(TEST_USER_NAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .primaryEmail(TEST_PRIMARY_EMAIL)
                .secondaryEmail(TEST_SECONDARY_EMAIL)
                .password(TEST_PASSWORD)
                .gender(TEST_GENDER)
                .profileImage(TEST_PROFILE_IMAGE)
                .about(TEST_ABOUT)
                .lastSeen(null)
                .modifiedBy(ADMIN)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }
}
