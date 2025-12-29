package com.example.reviews_service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic reviewCreatedTopic() {
        return new NewTopic("review-created", 1, (short) 1);
    }
}

