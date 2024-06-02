package com.phoenix.amazonbackend.repository;

import com.phoenix.amazonbackend.entities.Users;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.GENDER.MALE;
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
        final Users savedUserData = userRepository.save(userData);

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
        final String TEST_ABOUT = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia,\n" +
                "molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborum\n";

        return Users.builder()
                .userId(UUID.fromString("58824409-dd6b-4934-9923-ec1daf9693da"))
                .userName("TEST_USER_NAME")
                .firstName("TEST_FIRST_NAME")
                .lastName("TEST_LAST_NAME")
                .primaryEmail("testprimary@gmail.com")
                .secondaryEmail("testsecondary@gmail.com")
                .password("TEST_PASSWORD")
                .gender(MALE)
                .profileImage("0ecbe17e-5537-4533-9b9f-b3c2438e58eb.jpg")
                .about(TEST_ABOUT)
                .lastSeen(null)
                .modifiedBy("ADMIN")
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }
}
