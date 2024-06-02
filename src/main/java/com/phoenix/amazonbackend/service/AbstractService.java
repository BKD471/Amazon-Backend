package com.phoenix.amazonbackend.service;


import com.phoenix.amazonbackend.entities.Users;
import com.phoenix.amazonbackend.repository.IUserRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor
public abstract class AbstractService {
    private final IUserRepository userRepository;

    protected Users loadUserByUserIdOrUserNameOrPrimaryEmail(final UUID userId,
                                                             final String userName,
                                                             final String primaryEmail) {
        Optional<Users> users = userRepository.findByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);
        return users.get();
    }
}
