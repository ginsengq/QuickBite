package com.example.order_service.event;

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
        log.info("received user created event for user id: {}", event.getUserId());
        
        // business logic: when new user is created, log it for audit purposes
        // in real application, this could trigger:
        // - sending welcome email
        // - creating user profile
        // - initializing user preferences
        // - updating analytics/metrics
        
        log.info("processing user created event: email={}, name={} {}, role={}", 
                event.getEmail(), 
                event.getFirstName(), 
                event.getLastName(),
                event.getRole());
        
        // simulate business logic
        log.info("user {} successfully registered in order service", event.getUserId());
    }
}
