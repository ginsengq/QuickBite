package com.example.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {

    @NotBlank(message = "status is required")
    private String status;
}
