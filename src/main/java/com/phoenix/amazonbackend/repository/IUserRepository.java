package com.phoenix.amazonbackend.repository;

import com.phoenix.amazonbackend.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IUserRepository extends JpaRepository<Users, UUID> {
}
