package com.example.restaurant_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateMenuItemRequest {

    @NotBlank(message = "menu item name is required")
    @Size(min = 3, max = 255, message = "name must be between 3 and 255 characters")
    private String name;

    @Size(max = 1000, message = "description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "price is required")
    @Min(value = 0, message = "price must be positive")
    private Long price;

    @Size(max = 500, message = "image URL must not exceed 500 characters")
    private String imageUrl;

    @NotNull(message = "restaurant ID is required")
    private Long restaurantId;

    private List<Long> categoryIds;
}
