package com.phoenix.amazonbackend.repository;

import com.phoenix.amazonbackend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
