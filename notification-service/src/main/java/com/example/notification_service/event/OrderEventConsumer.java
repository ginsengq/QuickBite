package com.example.notification_service.event;

import com.example.notification_service.dto.OrderCreatedEvent;
import com.example.notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventConsumer {

    private final NotificationService notificationService;

    public OrderEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "order-created", groupId = "notification-service-group")
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("🔔 ========================================");
        log.info("🔔 RECEIVED ORDER CREATED EVENT");
        log.info("🔔 ========================================");
        log.info("📦 Order ID: {}", event.getOrderId());
        log.info("👤 User ID: {}", event.getUserId());
        log.info("💰 Total Price: {}", event.getTotalPrice());
        log.info("🔔 ========================================");

        notificationService.sendOrderCreatedNotification(
                event.getUserId(),
                event.getOrderId(),
                event.getTotalPrice()
        );
    }
}

