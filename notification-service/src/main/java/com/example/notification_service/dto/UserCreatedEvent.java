package com.example.notification_service.dto;

import lombok.Data;

@Data
public class UserCreatedEvent {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
}

