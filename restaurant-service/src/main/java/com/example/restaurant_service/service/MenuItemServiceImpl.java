package com.example.restaurant_service.service;

import com.example.restaurant_service.dto.*;
import com.example.restaurant_service.entity.Category;
import com.example.restaurant_service.entity.MenuItem;
import com.example.restaurant_service.entity.Restaurant;
import com.example.restaurant_service.exception.CategoryNotFoundException;
import com.example.restaurant_service.exception.MenuItemNotFoundException;
import com.example.restaurant_service.exception.RestaurantNotFoundException;
import com.example.restaurant_service.mapper.MenuItemMapper;
import com.example.restaurant_service.repository.CategoryRepository;
import com.example.restaurant_service.repository.MenuItemRepository;
import com.example.restaurant_service.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository,
                              RestaurantRepository restaurantRepository,
                              CategoryRepository categoryRepository,
                              MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public MenuItemResponse createMenuItem(CreateMenuItemRequest request) {
        log.info("creating new menu item: {}", request.getName());
        
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(request.getRestaurantId()));
        
        List<Category> categories = new ArrayList<>();
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            categories = categoryRepository.findAllById(request.getCategoryIds());
            if (categories.size() != request.getCategoryIds().size()) {
                log.error("some category ids not found");
                throw new CategoryNotFoundException("one or more category ids not found");
            }
        }
        
        MenuItem menuItem = menuItemMapper.toEntity(request, restaurant, categories);
        MenuItem saved = menuItemRepository.save(menuItem);
        
        log.info("menu item created successfully with id: {}", saved.getId());
        return menuItemMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse getMenuItemById(Long id) {
        log.info("fetching menu item by id: {}", id);
        
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));
        
        return menuItemMapper.toResponse(menuItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItemsByRestaurant(Long restaurantId) {
        log.info("fetching menu items for restaurant: {}", restaurantId);
        
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new RestaurantNotFoundException(restaurantId);
        }
        
        return menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId).stream()
                .map(menuItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> getMenuItemsByCategory(Long categoryId) {
        log.info("fetching menu items for category: {}", categoryId);
        
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }
        
        return menuItemRepository.findByCategoryId(categoryId).stream()
                .map(menuItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItemResponse updateMenuItem(Long id, UpdateMenuItemRequest request) {
        log.info("updating menu item with id: {}", id);
        
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));
        
        List<Category> categories = null;
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            categories = categoryRepository.findAllById(request.getCategoryIds());
            if (categories.size() != request.getCategoryIds().size()) {
                throw new CategoryNotFoundException("one or more category ids not found");
            }
        }
        
        menuItemMapper.updateEntity(menuItem, request, categories);
        MenuItem updated = menuItemRepository.save(menuItem);
        
        log.info("menu item updated successfully: {}", id);
        return menuItemMapper.toResponse(updated);
    }

    @Override
    public void deleteMenuItem(Long id) {
        log.info("deleting menu item with id: {}", id);
        
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));
        
        // soft delete - mark as unavailable
        menuItem.setIsAvailable(false);
        menuItemRepository.save(menuItem);
        
        log.info("menu item deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, Long> getMenuItemPrices(Iterable<Long> menuItemIds) {
        log.info("fetching prices for menu items");
        
        List<MenuItem> menuItems = menuItemRepository.findAllByIdIn(menuItemIds);
        Map<Long, Long> prices = new HashMap<>();
        
        for (MenuItem item : menuItems) {
            prices.put(item.getId(), item.getPrice());
        }
        
        return prices;
    }
}
