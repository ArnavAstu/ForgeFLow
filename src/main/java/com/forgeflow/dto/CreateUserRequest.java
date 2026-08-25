package com.forgeflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    // Name cannot be null, empty, or only spaces
    @NotBlank(message = "Name is required")
    private String name;

    // Email cannot be blank and must have a valid email format
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    // Password must contain at least 8 characters
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;
}