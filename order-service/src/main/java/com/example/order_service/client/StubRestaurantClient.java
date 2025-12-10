package com.example.order_service.client;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class StubRestaurantClient implements RestaurantClient {

    @Override
    public void validateRestaurantExists(Long restaurantId) {
        // TODO: позже сделать реальный REST вызов
    }

    @Override
    public Map<Long, Long> getMenuItemsPrices(Iterable<Long> menuItemIds) {
        // TODO: вернуть реальные цены, пока пусто
        return Collections.emptyMap();
    }
}