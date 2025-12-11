package com.example.restaurant_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateRestaurantRequest {

    @NotBlank(message = "restaurant name is required")
    @Size(min = 3, max = 255, message = "restaurant name must be between 3 and 255 characters")
    private String name;

    @Size(max = 1000, message = "description must not exceed 1000 characters")
    private String description;

    @NotBlank(message = "address is required")
    @Size(max = 500, message = "address must not exceed 500 characters")
    private String address;

    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{7,20}$", message = "invalid phone number format")
    private String phoneNumber;
}
