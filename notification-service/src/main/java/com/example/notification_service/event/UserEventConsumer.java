package com.example.notification_service.event;

import com.example.notification_service.dto.UserCreatedEvent;
import com.example.notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserEventConsumer {

    private final NotificationService notificationService;

    public UserEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-service-group")
    public void consumeUserCreatedEvent(UserCreatedEvent event) {
        log.info("🔔 ========================================");
        log.info("🔔 RECEIVED USER CREATED EVENT");
        log.info("🔔 ========================================");
        log.info("👤 User ID: {}", event.getUserId());
        log.info("📧 Email: {}", event.getEmail());
        log.info("👤 Name: {} {}", event.getFirstName(), event.getLastName());
        log.info("🔔 ========================================");

        notificationService.sendWelcomeNotification(
                event.getUserId(),
                event.getEmail(),
                event.getFirstName()
        );
    }
}

