package com.example.payment_service.event;

import com.example.payment_service.dto.OrderCreatedEvent;
import com.example.payment_service.entity.Payment;
import com.example.payment_service.entity.PaymentStatus;
import com.example.payment_service.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventConsumer {

    private final PaymentRepository paymentRepository;

    public OrderEventConsumer(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @KafkaListener(topics = "order-created", groupId = "payment-service-group")
    public void consumeOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("🔔 ========================================");
        log.info("🔔 RECEIVED ORDER CREATED EVENT");
        log.info("🔔 ========================================");
        log.info("📦 Order ID: {}", event.getOrderId());
        log.info("👤 User ID: {}", event.getUserId());
        log.info("🍽️ Restaurant ID: {}", event.getRestaurantId());
        log.info("💰 Total Price: {}", event.getTotalPrice());
        log.info("🔔 ========================================");

        // automatically create pending payment when order is created
        Payment payment = new Payment();
        payment.setOrderId(event.getOrderId());
        payment.setUserId(event.getUserId());
        payment.setAmount(event.getTotalPrice());
        payment.setCurrency("KZT");
        payment.setPaymentMethod("CARD");
        payment.setPaymentStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);
        log.info("✅ Created pending payment for order: {}", event.getOrderId());
    }
}

