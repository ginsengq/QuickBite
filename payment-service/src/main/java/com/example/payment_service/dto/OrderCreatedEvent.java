package com.example.payment_service.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private Long totalPrice;
    private String status;
    private Instant createdAt;
    private List<OrderItemEvent> items;

    @Data
    public static class OrderItemEvent {
        private Long menuItemId;
        private Integer quantity;
        private Long price;
    }
}

