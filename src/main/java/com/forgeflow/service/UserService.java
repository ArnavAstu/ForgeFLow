package com.forgeflow.service;

import com.forgeflow.dto.CreateUserRequest;
import com.forgeflow.dto.UserResponse;
import com.forgeflow.entity.User;
import com.forgeflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(CreateUserRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser);
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id)
                );

        return new UserResponse(user);
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserResponse::new)
                .toList();
    }
}

//     CreateUserRequest
//           ↓
//      User entity
//           ↓
//    userRepository.save()
//           ↓
//       PostgreSQL
//           ↓
//       saved User
//           ↓
//       UserResponse