package com.example.order_service.dto;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private String status;
}
