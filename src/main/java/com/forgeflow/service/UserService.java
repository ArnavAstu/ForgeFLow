package com.forgeflow.service;

import com.forgeflow.dto.CreateUserRequest;
import com.forgeflow.dto.UserResponse;
import com.forgeflow.entity.User;
import com.forgeflow.exception.DuplicateResourceException;
import com.forgeflow.exception.ResourceNotFoundException;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
        user.setPassword(request.getPassword());

        // Save user into PostgreSQL
        User savedUser = userRepository.save(user);

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