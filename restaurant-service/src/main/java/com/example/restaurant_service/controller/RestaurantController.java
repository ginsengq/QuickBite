package com.example.restaurant_service.controller;

import com.example.restaurant_service.dto.*;
import com.example.restaurant_service.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurant", description = "Restaurant management APIs")
@Slf4j
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new restaurant", description = "Admin only - creates a new restaurant")
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
        log.info("received request to create restaurant: {}", request.getName());
        RestaurantResponse response = restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID", description = "Returns restaurant details by ID")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable Long id) {
        log.info("received request to get restaurant: {}", id);
        RestaurantResponse response = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all restaurants", description = "Returns list of all restaurants")
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {
        log.info("received request to get all restaurants, activeOnly: {}", activeOnly);
        List<RestaurantResponse> response = activeOnly 
            ? restaurantService.getActiveRestaurants() 
            : restaurantService.getAllRestaurants();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search restaurants by name", description = "Search restaurants by keyword")
    public ResponseEntity<List<RestaurantResponse>> searchRestaurants(@RequestParam String keyword) {
        log.info("received request to search restaurants by keyword: {}", keyword);
        List<RestaurantResponse> response = restaurantService.searchRestaurantsByName(keyword);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update restaurant", description = "Admin only - updates restaurant details")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRestaurantRequest request) {
        log.info("received request to update restaurant: {}", id);
        RestaurantResponse response = restaurantService.updateRestaurant(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete restaurant", description = "Admin only - soft deletes a restaurant")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        log.info("received request to delete restaurant: {}", id);
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
