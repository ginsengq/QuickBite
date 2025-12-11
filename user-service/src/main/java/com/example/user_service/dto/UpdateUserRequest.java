package com.example.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Email(message = "invalid email format")
    private String email;

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "invalid phone format")
    private String phone;

    private String address;

    @Pattern(regexp = "USER|ADMIN", message = "role must be USER or ADMIN")
    private String role;

    private Boolean active;
}
