package com.example.notification_service.event;

import com.example.notification_service.dto.PaymentCompletedEvent;
import com.example.notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentEventConsumer {

    private final NotificationService notificationService;

    public PaymentEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "payment-completed", groupId = "notification-service-group")
    public void consumePaymentCompletedEvent(PaymentCompletedEvent event) {
        log.info("🔔 ========================================");
        log.info("🔔 RECEIVED PAYMENT COMPLETED EVENT");
        log.info("🔔 ========================================");
        log.info("💳 Payment ID: {}", event.getPaymentId());
        log.info("📦 Order ID: {}", event.getOrderId());
        log.info("👤 User ID: {}", event.getUserId());
        log.info("💰 Amount: {}", event.getAmount());
        log.info("🔔 ========================================");

        notificationService.sendPaymentCompletedNotification(
                event.getUserId(),
                event.getOrderId(),
                event.getAmount()
        );
    }
}

