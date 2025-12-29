package com.example.payment_service.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class PaymentCompletedEvent {
    private Long paymentId;
    private Long orderId;
    private Long userId;
    private Long amount;
    private String paymentStatus;
    private String transactionId;
    private Instant completedAt;
}

