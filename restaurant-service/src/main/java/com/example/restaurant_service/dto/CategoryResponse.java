package com.example.restaurant_service.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Instant createdAt;
    private Instant updatedAt;
}
