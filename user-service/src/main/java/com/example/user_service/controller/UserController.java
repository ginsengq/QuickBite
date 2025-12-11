package com.example.user_service.controller;

import com.example.user_service.dto.CreateUserRequest;
import com.example.user_service.dto.UpdateUserRequest;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User management APIs")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new user", description = "Creates a new user (admin only)")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("received request to create user: {}", request.getEmail());
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get user by ID", description = "Returns user details by ID")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        log.info("received request to get user: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get user by email", description = "Returns user details by email")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        log.info("received request to get user by email: {}", email);
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users", description = "Returns all users (admin only)")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("received request to get all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get users by role", description = "Returns users by role (admin only)")
    public ResponseEntity<List<UserResponse>> getUsersByRole(@PathVariable String role) {
        log.info("received request to get users by role: {}", role);
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get active users", description = "Returns all active users (admin only)")
    public ResponseEntity<List<UserResponse>> getActiveUsers() {
        log.info("received request to get active users");
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update user", description = "Updates user information (admin only)")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("received request to update user: {}", id);
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user", description = "Deletes a user (admin only)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("received request to delete user: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
