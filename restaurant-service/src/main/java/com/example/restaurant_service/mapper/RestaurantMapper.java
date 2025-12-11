package com.example.restaurant_service.mapper;

import com.example.restaurant_service.dto.*;
import com.example.restaurant_service.entity.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public Restaurant toEntity(CreateRestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setPhoneNumber(request.getPhoneNumber());
        restaurant.setIsActive(true);
        restaurant.setRating(0.0);
        return restaurant;
    }

    public void updateEntity(Restaurant restaurant, UpdateRestaurantRequest request) {
        if (request.getName() != null) {
            restaurant.setName(request.getName());
        }
        if (request.getDescription() != null) {
            restaurant.setDescription(request.getDescription());
        }
        if (request.getAddress() != null) {
            restaurant.setAddress(request.getAddress());
        }
        if (request.getPhoneNumber() != null) {
            restaurant.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getIsActive() != null) {
            restaurant.setIsActive(request.getIsActive());
        }
        if (request.getRating() != null) {
            restaurant.setRating(request.getRating());
        }
    }

    public RestaurantResponse toResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setDescription(restaurant.getDescription());
        response.setAddress(restaurant.getAddress());
        response.setPhoneNumber(restaurant.getPhoneNumber());
        response.setIsActive(restaurant.getIsActive());
        response.setRating(restaurant.getRating());
        response.setCreatedAt(restaurant.getCreatedAt());
        response.setUpdatedAt(restaurant.getUpdatedAt());
        return response;
    }
}
