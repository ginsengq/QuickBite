package com.example.restaurant_service.service;

import com.example.restaurant_service.dto.*;
import com.example.restaurant_service.entity.Restaurant;
import com.example.restaurant_service.exception.RestaurantNotFoundException;
import com.example.restaurant_service.mapper.RestaurantMapper;
import com.example.restaurant_service.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, 
                                RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        log.info("creating new restaurant: {}", request.getName());
        
        Restaurant restaurant = restaurantMapper.toEntity(request);
        Restaurant saved = restaurantRepository.save(restaurant);
        
        log.info("restaurant created successfully with id: {}", saved.getId());
        return restaurantMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(Long id) {
        log.info("fetching restaurant by id: {}", id);
        
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        
        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants() {
        log.info("fetching all restaurants");
        
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getActiveRestaurants() {
        log.info("fetching active restaurants");
        
        return restaurantRepository.findByIsActiveTrue().stream()
                .map(restaurantMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RestaurantResponse updateRestaurant(Long id, UpdateRestaurantRequest request) {
        log.info("updating restaurant with id: {}", id);
        
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        
        restaurantMapper.updateEntity(restaurant, request);
        Restaurant updated = restaurantRepository.save(restaurant);
        
        log.info("restaurant updated successfully: {}", id);
        return restaurantMapper.toResponse(updated);
    }

    @Override
    public void deleteRestaurant(Long id) {
        log.info("deleting restaurant with id: {}", id);
        
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        
        // soft delete - just mark as inactive
        restaurant.setIsActive(false);
        restaurantRepository.save(restaurant);
        
        log.info("restaurant deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> searchRestaurantsByName(String keyword) {
        log.info("searching restaurants by keyword: {}", keyword);
        
        return restaurantRepository.searchByName(keyword).stream()
                .map(restaurantMapper::toResponse)
                .collect(Collectors.toList());
    }
}
