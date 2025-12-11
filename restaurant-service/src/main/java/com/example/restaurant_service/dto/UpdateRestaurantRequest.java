package com.example.restaurant_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateRestaurantRequest {

    @Size(min = 3, max = 255, message = "restaurant name must be between 3 and 255 characters")
    private String name;

    @Size(max = 1000, message = "description must not exceed 1000 characters")
    private String description;

    @Size(max = 500, message = "address must not exceed 500 characters")
    private String address;

    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{7,20}$", message = "invalid phone number format")
    private String phoneNumber;

    private Boolean isActive;

    @DecimalMin(value = "0.0", message = "rating must be at least 0.0")
    @DecimalMax(value = "5.0", message = "rating must not exceed 5.0")
    private Double rating;
}
