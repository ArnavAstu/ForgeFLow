package com.forgeflow.service;

import com.forgeflow.dto.CreateUserRequest;
import com.forgeflow.dto.UserResponse;
import com.forgeflow.entity.Role;
import com.forgeflow.entity.User;
import com.forgeflow.exception.DuplicateResourceException;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

//Business logic and Entity Conversion
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Creates a new user
    public UserResponse createUser(CreateUserRequest request) {

        // Check whether the email is already registered
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "User with email " + request.getEmail() + " already exists"
            );
        }

        // Convert request DTO into User entity
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );
        user.setRole(Role.USER);
//        Entity Conversion:
//        CreateUserRequest → User → userRepository.save(user)
//        CreateUserRequest is a DTO, not a database entity, so the service converts it into a User entity before saving.

        // Save user into PostgreSQL
        User savedUser = userRepository.save(user);
//        userRepository.save(user) → Spring Data JPA → Hibernate → JDBC → PostgreSQL
//        Hibernate generates the appropriate SQL, conceptually:
//        INSERT INTO users (name, email, password)
//        VALUES ('Arnav', 'arnav@gmail.com', '...');

        // Convert entity into response DTO
        return new UserResponse(savedUser);
    }

    // Gets all users
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }

    // Gets a single user by ID
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return new UserResponse(user);
    }


}