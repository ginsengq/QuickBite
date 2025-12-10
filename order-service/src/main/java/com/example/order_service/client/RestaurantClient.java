package com.example.order_service.client;

import java.util.Map;

public interface RestaurantClient {

    void validateRestaurantExists(Long restaurantId);

    Map<Long, Long> getMenuItemsPrices(Iterable<Long> menuItemIds);
}
