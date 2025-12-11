package com.example.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {

    @NotNull(message = "user ID is required")
    private Long userId;

    @NotNull(message = "restaurant ID is required")
    private Long restaurantId;

    @NotEmpty(message = "order must contain at least one item")
    @Valid
    private List<CreateOrderItemRequest> items;
}
