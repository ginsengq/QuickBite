package com.example.restaurant_service.mapper;

import com.example.restaurant_service.dto.*;
import com.example.restaurant_service.entity.Category;
import com.example.restaurant_service.entity.MenuItem;
import com.example.restaurant_service.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuItemMapper {

    private final CategoryMapper categoryMapper;

    public MenuItemMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public MenuItem toEntity(CreateMenuItemRequest request, Restaurant restaurant, List<Category> categories) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setIsAvailable(true);
        menuItem.setRestaurant(restaurant);
        menuItem.setCategories(categories);
        return menuItem;
    }

    public void updateEntity(MenuItem menuItem, UpdateMenuItemRequest request, List<Category> categories) {
        if (request.getName() != null) {
            menuItem.setName(request.getName());
        }
        if (request.getDescription() != null) {
            menuItem.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            menuItem.setPrice(request.getPrice());
        }
        if (request.getImageUrl() != null) {
            menuItem.setImageUrl(request.getImageUrl());
        }
        if (request.getIsAvailable() != null) {
            menuItem.setIsAvailable(request.getIsAvailable());
        }
        if (categories != null && !categories.isEmpty()) {
            menuItem.getCategories().clear();
            menuItem.getCategories().addAll(categories);
        }
    }

    public MenuItemResponse toResponse(MenuItem menuItem) {
        MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItem.getId());
        response.setName(menuItem.getName());
        response.setDescription(menuItem.getDescription());
        response.setPrice(menuItem.getPrice());
        response.setImageUrl(menuItem.getImageUrl());
        response.setIsAvailable(menuItem.getIsAvailable());
        response.setRestaurantId(menuItem.getRestaurant().getId());
        response.setRestaurantName(menuItem.getRestaurant().getName());
        
        List<CategoryResponse> categoryResponses = menuItem.getCategories().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
        response.setCategories(categoryResponses);
        
        response.setCreatedAt(menuItem.getCreatedAt());
        response.setUpdatedAt(menuItem.getUpdatedAt());
        return response;
    }
}
