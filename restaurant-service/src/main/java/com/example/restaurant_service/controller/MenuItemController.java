package com.example.restaurant_service.controller;

import com.example.restaurant_service.dto.*;
import com.example.restaurant_service.service.MenuItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu-items")
@Tag(name = "Menu Item", description = "Menu item management APIs")
@Slf4j
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new menu item", description = "Admin only - creates a new menu item")
    public ResponseEntity<MenuItemResponse> createMenuItem(@Valid @RequestBody CreateMenuItemRequest request) {
        log.info("received request to create menu item: {}", request.getName());
        MenuItemResponse response = menuItemService.createMenuItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu item by ID", description = "Returns menu item details by ID")
    public ResponseEntity<MenuItemResponse> getMenuItem(@PathVariable Long id) {
        log.info("received request to get menu item: {}", id);
        MenuItemResponse response = menuItemService.getMenuItemById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get menu items by restaurant", description = "Returns all available menu items for a restaurant")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        log.info("received request to get menu items for restaurant: {}", restaurantId);
        List<MenuItemResponse> response = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get menu items by category", description = "Returns all available menu items for a category")
    public ResponseEntity<List<MenuItemResponse>> getMenuItemsByCategory(@PathVariable Long categoryId) {
        log.info("received request to get menu items for category: {}", categoryId);
        List<MenuItemResponse> response = menuItemService.getMenuItemsByCategory(categoryId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prices")
    @Operation(summary = "Get menu item prices", description = "Returns prices for specified menu item IDs")
    public ResponseEntity<Map<Long, Long>> getMenuItemPrices(@RequestParam List<Long> ids) {
        log.info("received request to get prices for menu items: {}", ids);
        Map<Long, Long> prices = menuItemService.getMenuItemPrices(ids);
        return ResponseEntity.ok(prices);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update menu item", description = "Admin only - updates menu item details")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMenuItemRequest request) {
        log.info("received request to update menu item: {}", id);
        MenuItemResponse response = menuItemService.updateMenuItem(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete menu item", description = "Admin only - soft deletes a menu item")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        log.info("received request to delete menu item: {}", id);
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}
