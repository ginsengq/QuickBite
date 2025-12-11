package com.example.restaurant_service.service;

import com.example.restaurant_service.dto.*;

import java.util.List;
import java.util.Map;

public interface MenuItemService {

    MenuItemResponse createMenuItem(CreateMenuItemRequest request);

    MenuItemResponse getMenuItemById(Long id);

    List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId);

    List<MenuItemResponse> getMenuItemsByCategory(Long categoryId);

    MenuItemResponse updateMenuItem(Long id, UpdateMenuItemRequest request);

    void deleteMenuItem(Long id);

    Map<Long, Long> getMenuItemPrices(Iterable<Long> menuItemIds);
}
