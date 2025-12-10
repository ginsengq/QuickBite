package com.example.order_service.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private String status;
    private Long totalPrice;
    private Instant createdAt;
    private Instant updatedAt;
    private List<OrderItemResponse> items;
}
