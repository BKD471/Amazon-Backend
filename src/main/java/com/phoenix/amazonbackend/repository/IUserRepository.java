package com.phoenix.amazonbackend.repository;

import com.phoenix.amazonbackend.entities.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<Users, UUID> {
    /**
     * @param userId       - user id of user
     * @param userName     - userName of user
     * @param primaryEmail - primary email of user
     * @return Optional<Users> -  users
     */
    Optional<Users> findByUserIdOrUserNameOrPrimaryEmail(final UUID userId,
                                                         final String userName,
                                                         final String primaryEmail);

    /**
     * @param userId       - id of user
     * @param userName     - userName of user
     * @param primaryEmail - primary email of user
     */
    void deleteByUserIdOrUserNameOrPrimaryEmail(final UUID userId,
                                                final String userName,
                                                final String primaryEmail);

    /**
     * @param userName - userName of user
     * @param pageable - pageable object
     * @return Page<Users> - page of users
     **/
    Optional<Page<Users>> findAllByUserNameContaining(final String userName,
                                                      final Pageable pageable);

    /**
     * @param value    - value of user fields
     * @param pageable - pageable object
     * @return Page<Users> - page of users
     **/
    @Query(value = "SELECT * FROM users WHERE first_name=?1", nativeQuery = true)
    Optional<Page<Users>> searchUserByFirstName(final String value,
                                                final Pageable pageable);

    /**
     * @param value    - value of user fields
     * @param pageable - pageable object
     * @return Page<Users> - page of users
     **/
    @Query(value = "SELECT * FROM users WHERE last_name=?1", nativeQuery = true)
    Optional<Page<Users>> searchUserByLastName(final String value,
                                               final Pageable pageable);

    /**
     * @param value    - value of user fields
     * @param pageable - pageable object
     * @return Page<Users> - page of users
     **/
    @Query(value = "SELECT * FROM users WHERE gender=?1", nativeQuery = true)
    Optional<Page<Users>> searchUserByGender(final String value,
                                             final Pageable pageable);

    /**
     * @param value    - value of user fields
     * @param pageable - pageable object
     * @return Page<Users> - page of users
     **/
    @Query(value = "SELECT * FROM users WHERE user_name=?1", nativeQuery = true)
    Optional<Page<Users>> searchUserByUserName(final String value,
                                               final Pageable pageable);

    /**
     * @param value    - value of user fields
     * @param pageable - pageable object
     * @return Page<Users> - page of users
     **/
    @Query(value = "SELECT * FROM users WHERE user_primary_email=?1", nativeQuery = true)
    Optional<Page<Users>> searchUserByEmail(final String value,
                                            final Pageable pageable);
}
