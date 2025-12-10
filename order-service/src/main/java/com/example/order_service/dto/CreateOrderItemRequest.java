package com.example.order_service.dto;

import lombok.Data;

@Data
public class CreateOrderItemRequest {
    private Long menuItemId;
    private Integer quantity;
}
