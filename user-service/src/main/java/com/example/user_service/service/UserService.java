package com.example.user_service.service;

import com.example.user_service.dto.CreateUserRequest;
import com.example.user_service.dto.UpdateUserRequest;
import com.example.user_service.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    UserResponse getUserByEmail(String email);

    List<UserResponse> getAllUsers();

    List<UserResponse> getUsersByRole(String role);

    List<UserResponse> getActiveUsers();

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);
}
