package com.example.restaurant_service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank(message = "category name is required")
    @Size(min = 2, max = 100, message = "name must be between 2 and 100 characters")
    private String name;

    @Size(max = 1000, message = "description must not exceed 1000 characters")
    private String description;

    @Size(max = 500, message = "image URL must not exceed 500 characters")
    private String imageUrl;
}
