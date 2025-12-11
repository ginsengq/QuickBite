package com.example.order_service.event;

import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.dto.OrderCreatedEvent;
import com.example.order_service.dto.OrderItemEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * kafka-based order event publisher
 * activate with 'kafka' spring profile
 */
@Component
@Profile("kafka")
@Slf4j
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private static final String ORDER_CREATED_TOPIC = "order-created";
    // can add more topics for different events

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public KafkaOrderEventPublisher(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderCreated(Order order) {
        log.info("publishing order created event to kafka: orderId={}", order.getId());
        
        OrderCreatedEvent event = mapToEvent(order);
        String key = String.valueOf(order.getUserId());
        kafkaTemplate.send(ORDER_CREATED_TOPIC, key, event);
        
        log.info("order created event published successfully");
    }

    @Override
    public void publishOrderStatusChanged(Order order) {
        log.info("order status changed: orderId={}, status={}", order.getId(), order.getStatus());
        // TODO: create OrderStatusChangedEvent and publish to kafka
    }

    /**
     * maps order entity to order created event
     */
    private OrderCreatedEvent mapToEvent(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(order.getId());
        event.setUserId(order.getUserId());
        event.setRestaurantId(order.getRestaurantId());
        event.setTotalPrice(order.getTotalPrice());
        event.setStatus(order.getStatus().name());
        event.setCreatedAt(order.getCreatedAt());

        if (order.getItems() != null) {
            event.setItems(order.getItems().stream()
                    .map(this::mapItem)
                    .collect(Collectors.toList()));
        }

        return event;
    }

    /**
     * maps order item entity to order item event
     */
    private OrderItemEvent mapItem(OrderItem item) {
        OrderItemEvent ev = new OrderItemEvent();
        ev.setMenuItemId(item.getMenuItemId());
        ev.setQuantity(item.getQuantity());
        ev.setPrice(item.getPrice());
        return ev;
    }
}
