package com.forgeflow.repository;

import com.forgeflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    // Checks whether a user with this email already exists
    boolean existsByEmail(String email);
}
