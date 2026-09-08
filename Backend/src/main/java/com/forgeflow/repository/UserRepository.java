package com.forgeflow.repository;

import com.forgeflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Checks whether a user with this email already exists
    boolean existsByEmail(String email);
}

//JpaRepository provides ready-made methods like save(), findById(), findAll(), deleteById(),
//existsById(), and count() for performing common database operations without writing them yourself.

//Your Code → UserRepository → Spring Data JPA → Generated Implementation → Hibernate → JDBC → PostgreSQL