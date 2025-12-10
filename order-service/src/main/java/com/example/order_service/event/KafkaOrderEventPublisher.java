package com.example.order_service.event;

import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.dto.OrderCreatedEvent;
import com.example.order_service.dto.OrderItemEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Profile("kafka") // включай профиль kafka, когда хочешь использовать реальную Kafka
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private static final String ORDER_CREATED_TOPIC = "order-created";
    // при желании добавь и второй топик для статусов

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public KafkaOrderEventPublisher(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrderCreated(Order order) {
        OrderCreatedEvent event = mapToEvent(order);
        String key = String.valueOf(order.getUserId());
        kafkaTemplate.send(ORDER_CREATED_TOPIC, key, event);
    }

    @Override
    public void publishOrderStatusChanged(Order order) {
        // TODO: добавить OrderStatusChangedEvent при необходимости
    }

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

    private OrderItemEvent mapItem(OrderItem item) {
        OrderItemEvent ev = new OrderItemEvent();
        ev.setMenuItemId(item.getMenuItemId());
        ev.setQuantity(item.getQuantity());
        ev.setPrice(item.getPrice());
        return ev;
    }
}
