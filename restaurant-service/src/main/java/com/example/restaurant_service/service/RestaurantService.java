package com.example.restaurant_service.service;

import com.example.restaurant_service.dto.*;

import java.util.List;

public interface RestaurantService {

    RestaurantResponse createRestaurant(CreateRestaurantRequest request);

    RestaurantResponse getRestaurantById(Long id);

    List<RestaurantResponse> getAllRestaurants();

    List<RestaurantResponse> getActiveRestaurants();

    RestaurantResponse updateRestaurant(Long id, UpdateRestaurantRequest request);

    void deleteRestaurant(Long id);

    List<RestaurantResponse> searchRestaurantsByName(String keyword);
}
