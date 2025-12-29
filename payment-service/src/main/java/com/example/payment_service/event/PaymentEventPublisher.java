package com.example.payment_service.event;

import com.example.payment_service.dto.PaymentCompletedEvent;

public interface PaymentEventPublisher {
    void publishPaymentCompleted(PaymentCompletedEvent event);
}

