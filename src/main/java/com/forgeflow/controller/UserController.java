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
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        UserResponse response = userService.getUserById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }
}

/*
Our complete flow is now:

        POST /api/users
               ↓
        UserController
               ↓
        CreateUserRequest
               ↓
        UserService
               ↓
        User entity
               ↓
        UserRepository
               ↓
        Hibernate
               ↓
        PostgreSQL

And the response comes back:

        PostgreSQL
            ↓
        UserRepository
            ↓
        UserService
            ↓
        UserResponse
            ↓
        UserController
            ↓
        JSON

*/