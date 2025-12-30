package com.example.order_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST client for restaurant service
 */
@Component
@Slf4j
public class StubRestaurantClient implements RestaurantClient {

    private final RestTemplate restTemplate;
    private final String restaurantServiceUrl;

    public StubRestaurantClient(RestTemplate restTemplate,
                                @Value("${restaurant.service.url:http://localhost:8081}") String restaurantServiceUrl) {
        this.restTemplate = restTemplate;
        this.restaurantServiceUrl = restaurantServiceUrl;
    }

    @Override
    public void validateRestaurantExists(Long restaurantId) {
        log.info("validating restaurant exists: {}", restaurantId);
        try {
            String url = restaurantServiceUrl + "/api/restaurants/" + restaurantId;
            restTemplate.getForEntity(url, Object.class);
            log.info("restaurant {} exists", restaurantId);
        } catch (Exception e) {
            log.error("failed to validate restaurant {}: {}", restaurantId, e.getMessage());
            throw new RuntimeException("Restaurant not found: " + restaurantId);
        }
    }

    @Override
    public Map<Long, Long> getMenuItemsPrices(Iterable<Long> menuItemIds) {
        List<Long> ids = StreamSupport.stream(menuItemIds.spliterator(), false)
                .collect(Collectors.toList());
        
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }

        log.info("fetching menu item prices: {}", ids);
        
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl(restaurantServiceUrl + "/api/menu-items/prices")
                    .queryParam("ids", ids.toArray())
                    .toUriString();

            ResponseEntity<Map<Long, Long>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<Long, Long>>() {}
            );

            Map<Long, Long> prices = response.getBody();
            log.info("fetched prices for {} menu items", prices != null ? prices.size() : 0);
            return prices != null ? prices : Collections.emptyMap();
        } catch (Exception e) {
            log.error("failed to fetch menu item prices: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }
}