package com.example.restaurant_service.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class RestaurantResponse {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String phoneNumber;
    private Boolean isActive;
    private Double rating;
    private Instant createdAt;
    private Instant updatedAt;
}
