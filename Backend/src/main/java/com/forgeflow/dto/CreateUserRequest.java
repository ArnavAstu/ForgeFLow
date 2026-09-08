package com.forgeflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//this is a DTO
//DTO = Data Transfer Object
//Its purpose:
//Carry data from the client into your application.

//For example:
//
//        {
//        "name": "Arnav",
//        "email": "arnav@example.com",
//        "password": "password123"
//        }
//
//Spring converts that JSON into: CreateUserRequest


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

//Using @Getter and @Setter
//Lombok automatically generates methods like:
//public String getName() {
//    return name;
//}
//
//public void setName(String name) {
//    this.name = name;
//}
//
//public String getEmail() {
//    return email;
//}
//
//public void setEmail(String email) {
//    this.email = email;
//}
//
//public String getPassword() {
//    return password;
//}
//
//public void setPassword(String password) {
//    this.password = password;
//}



//Why are setters useful here?
//
//Suppose the client sends:
//
//        {
//        "name": "Arnav",
//        "email": "arnav@example.com",
//        "password": "password123"
//        }

//Spring/Jackson converts that JSON into:

//CreateUserRequest request

//Conceptually, it needs to put the incoming values into your object's fields:

//request.setName("Arnav");
//request.setEmail("arnav@example.com");
//request.setPassword("password123");
//
//That's why setters are useful.
//
//Why getters?
//
//Later, your application might need to read the values:
//
//        request.getName();
//request.getEmail();
//request.getPassword();
//
//        For example, in your service:
//
//public void createUser(CreateUserRequest request) {
//
//    String name = request.getName();
//    String email = request.getEmail();
//
//    // create user...
//}
//
//That's where getters are useful.
