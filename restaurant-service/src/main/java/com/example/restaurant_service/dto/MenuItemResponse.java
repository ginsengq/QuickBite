package com.example.restaurant_service.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class MenuItemResponse {

    private Long id;
    private String name;
    private String description;
    private Long price;
    private String imageUrl;
    private Boolean isAvailable;
    private Long restaurantId;
    private String restaurantName;
    private List<CategoryResponse> categories;
    private Instant createdAt;
    private Instant updatedAt;
}
