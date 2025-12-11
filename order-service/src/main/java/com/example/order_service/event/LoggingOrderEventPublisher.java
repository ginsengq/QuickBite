package com.example.order_service.event;

import com.example.order_service.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * logging-based order event publisher (used when kafka profile is not active)
 * useful for development and testing without kafka dependency
 */
@Component
@Profile("!kafka")
@Slf4j
public class LoggingOrderEventPublisher implements OrderEventPublisher {

    @Override
    public void publishOrderCreated(Order order) {
        log.info("ORDER CREATED EVENT: orderId={}, userId={}, restaurantId={}, status={}", 
                order.getId(), order.getUserId(), order.getRestaurantId(), order.getStatus());
    }

    @Override
    public void publishOrderStatusChanged(Order order) {
        log.info("ORDER STATUS CHANGED EVENT: orderId={}, newStatus={}", 
                order.getId(), order.getStatus());
    }
}
