package com.example.order_service.event;

import com.example.order_service.dto.UpdateOrderStatusRequest;
import com.example.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class PaymentEventConsumer {

    private final OrderService orderService;

    public PaymentEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "payment-completed", groupId = "order-service-group")
    public void handlePaymentCompleted(Map<String, Object> event) {
        try {
            log.info("Received payment completed event: {}", event);
            
            Long orderId = ((Number) event.get("orderId")).longValue();
            String paymentStatus = (String) event.get("paymentStatus");
            
            if ("COMPLETED".equals(paymentStatus)) {
                // Update order status to CONFIRMED when payment is completed
                UpdateOrderStatusRequest request = new UpdateOrderStatusRequest();
                request.setStatus("CONFIRMED");
                
                orderService.updateStatus(orderId, request);
                log.info("Order {} status updated to CONFIRMED after successful payment", orderId);
            }
        } catch (Exception e) {
            log.error("Error processing payment completed event: {}", e.getMessage(), e);
        }
    }
}
