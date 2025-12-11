package com.example.order_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * stub implementation of restaurant client
 * will be replaced with actual REST client when restaurant service is ready
 */
@Component
@Slf4j
public class StubRestaurantClient implements RestaurantClient {

    @Override
    public void validateRestaurantExists(Long restaurantId) {
        log.info("validating restaurant exists (stub): {}", restaurantId);
        // TODO: make real REST call to restaurant service
    }

    @Override
    public Map<Long, Long> getMenuItemsPrices(Iterable<Long> menuItemIds) {
        log.info("fetching menu item prices (stub): {}", menuItemIds);
        // TODO: make real REST call to get actual prices
        return Collections.emptyMap();
    }
}