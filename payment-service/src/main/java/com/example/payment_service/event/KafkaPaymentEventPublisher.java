package com.example.payment_service.event;

import com.example.payment_service.dto.PaymentCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaPaymentEventPublisher implements PaymentEventPublisher {

    private static final String PAYMENT_COMPLETED_TOPIC = "payment-completed";

    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    public KafkaPaymentEventPublisher(KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishPaymentCompleted(PaymentCompletedEvent event) {
        log.info("publishing payment completed event to kafka: paymentId={}, orderId={}", 
                event.getPaymentId(), event.getOrderId());
        
        String key = String.valueOf(event.getOrderId());
        kafkaTemplate.send(PAYMENT_COMPLETED_TOPIC, key, event);
        
        log.info("payment completed event published successfully");
    }
}

