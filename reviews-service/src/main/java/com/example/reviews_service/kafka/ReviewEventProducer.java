package com.example.reviews_service.kafka;

import com.example.reviews_service.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReviewEventProducer {

    private static final String TOPIC = "review-created";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishReviewCreated(ReviewDto dto) {
        kafkaTemplate.send(TOPIC, dto);
    }
}

