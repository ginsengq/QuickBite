package com.example.order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long userId;
    private Long restaurantId;
    private List<CreateOrderItemRequest> items;
}
