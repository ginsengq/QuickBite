package com.example.payment_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProcessPaymentRequest {
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    // Card fields are optional - only required for CARD payment method
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
}

