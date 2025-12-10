package com.example.order_service.dto;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long menuItemId;
    private Integer quantity;
    private Long price;
}
