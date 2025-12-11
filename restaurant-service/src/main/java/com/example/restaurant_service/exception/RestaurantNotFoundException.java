package com.example.restaurant_service.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(Long id) {
        super("restaurant with id " + id + " not found");
    }
}
