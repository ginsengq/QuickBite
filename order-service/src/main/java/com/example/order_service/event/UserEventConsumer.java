package com.example.order_service.event;

import com.example.order_service.controller.KafkaTestController;
import com.example.order_service.dto.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * kafka consumer for user events
 * listens to user-events topic and processes user created events
 */
@Component
@Slf4j
public class UserEventConsumer {

    @KafkaListener(topics = "user-events", groupId = "order-service-group")
    public void consumeUserCreatedEvent(UserCreatedEvent event) {
        log.info("🔔 ========================================");
        log.info("🔔 RECEIVED USER CREATED EVENT");
        log.info("🔔 ========================================");
        log.info("📨 User ID: {}", event.getUserId());
        log.info("📧 Email: {}", event.getEmail());
        log.info("👤 Name: {} {}", event.getFirstName(), event.getLastName());
        log.info("📱 Phone: {}", event.getPhoneNumber());
        log.info("🎭 Role: {}", event.getRole());
        log.info("🔔 ========================================");
        
        // Record message for testing endpoint
        KafkaTestController.recordReceivedMessage("user-events", event);
        
        // business logic: when new user is created, log it for audit purposes
        // in real application, this could trigger:
        // - sending welcome email
        // - creating user profile
        // - initializing user preferences
        // - updating analytics/metrics
        
        log.info("✅ User {} successfully processed in order-service", event.getUserId());
    }
}
