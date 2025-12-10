package com.example.order_service.dto;

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
}