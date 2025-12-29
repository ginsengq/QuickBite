package com.example.payment_service.dto;

import com.example.payment_service.entity.PaymentStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private Long userId;
    private Long amount;
    private String currency;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private Instant createdAt;
    private Instant updatedAt;
}

