package com.example.user_service.event;

import com.example.user_service.dto.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaUserEventPublisher implements UserEventPublisher {

    private static final String TOPIC = "user-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaUserEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishUserCreated(UserCreatedEvent event) {
        log.info("publishing user created event for user id: {}", event.getUserId());
        
        kafkaTemplate.send(TOPIC, "user.created", event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("user created event published successfully");
                    } else {
                        log.error("failed to publish user created event", ex);
                    }
                });
    }
}
