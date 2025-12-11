package com.example.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "email is required")
    @Email(message = "invalid email format")
    private String email;

    @NotBlank(message = "first name is required")
    private String firstName;

    @NotBlank(message = "last name is required")
    private String lastName;

    @NotBlank(message = "phone is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "invalid phone format")
    private String phone;

    private String address;

    @NotBlank(message = "role is required")
    @Pattern(regexp = "USER|ADMIN", message = "role must be USER or ADMIN")
    private String role;
}
