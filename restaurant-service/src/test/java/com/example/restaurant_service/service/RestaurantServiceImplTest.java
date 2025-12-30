package com.example.restaurant_service.service;

import com.example.restaurant_service.dto.CreateRestaurantRequest;
import com.example.restaurant_service.dto.RestaurantResponse;
import com.example.restaurant_service.dto.UpdateRestaurantRequest;
import com.example.restaurant_service.entity.Restaurant;
import com.example.restaurant_service.exception.RestaurantNotFoundException;
import com.example.restaurant_service.mapper.RestaurantMapper;
import com.example.restaurant_service.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant;
    private RestaurantResponse restaurantResponse;
    private CreateRestaurantRequest createRequest;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setDescription("Test Description");
        restaurant.setAddress("Test Address");
        restaurant.setPhoneNumber("+1234567890");
        restaurant.setIsActive(true);

        restaurantResponse = new RestaurantResponse();
        restaurantResponse.setId(1L);
        restaurantResponse.setName("Test Restaurant");
        restaurantResponse.setDescription("Test Description");
        restaurantResponse.setAddress("Test Address");
        restaurantResponse.setPhoneNumber("+1234567890");
        restaurantResponse.setActive(true);

        createRequest = new CreateRestaurantRequest();
        createRequest.setName("Test Restaurant");
        createRequest.setDescription("Test Description");
        createRequest.setAddress("Test Address");
        createRequest.setPhoneNumber("+1234567890");
    }

    @Test
    void createRestaurant_Success() {
        // Arrange
        when(restaurantMapper.toEntity(any(CreateRestaurantRequest.class))).thenReturn(restaurant);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(restaurantMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        // Act
        RestaurantResponse result = restaurantService.createRestaurant(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(restaurantResponse.getName(), result.getName());
        verify(restaurantRepository).save(any(Restaurant.class));
        verify(restaurantMapper).toResponse(any(Restaurant.class));
    }

    @Test
    void getRestaurantById_Success() {
        // Arrange
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        // Act
        RestaurantResponse result = restaurantService.getRestaurantById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Restaurant", result.getName());
        verify(restaurantRepository).findById(1L);
    }

    @Test
    void getRestaurantById_NotFound() {
        // Arrange
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.getRestaurantById(999L);
        });
        verify(restaurantRepository).findById(999L);
    }

    @Test
    void getAllRestaurants_Success() {
        // Arrange
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        when(restaurantRepository.findAll()).thenReturn(restaurants);
        when(restaurantMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponse);

        // Act
        List<RestaurantResponse> result = restaurantService.getAllRestaurants();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(restaurantRepository).findAll();
    }

    @Test
    void updateRestaurant_Success() {
        // Arrange
        UpdateRestaurantRequest updateRequest = new UpdateRestaurantRequest();
        updateRequest.setName("Updated Restaurant");
        updateRequest.setDescription("Updated Description");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant);
        when(restaurantMapper.toResponse(any(Restaurant.class))).thenReturn(restaurantResponse);
        doNothing().when(restaurantMapper).updateEntity(any(Restaurant.class), any(UpdateRestaurantRequest.class));

        // Act
        RestaurantResponse result = restaurantService.updateRestaurant(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(restaurantRepository).findById(1L);
        verify(restaurantRepository).save(any(Restaurant.class));
        verify(restaurantMapper).updateEntity(any(Restaurant.class), any(UpdateRestaurantRequest.class));
    }
}
