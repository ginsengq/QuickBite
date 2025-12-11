package com.example.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String role;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
