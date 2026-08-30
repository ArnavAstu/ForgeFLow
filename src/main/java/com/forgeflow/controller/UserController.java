package com.forgeflow.controller;

import com.forgeflow.dto.CreateUserRequest;
import com.forgeflow.dto.UserResponse;
import com.forgeflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

//The controller basically says:
// "I received this request. Service, you handle the actual work."

public class UserController {

    private final UserService userService;

    // Create user
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {
//        @RequestBody → Converts JSON request body into a Java object.
//        @Valid → Validates that Java object using annotations like @NotBlank, @Email, and @Size.
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(request));
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserById(id)
        );
    }

    public static class AuthResponse {
    }
}