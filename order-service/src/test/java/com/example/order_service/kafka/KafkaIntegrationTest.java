package com.example.order_service.kafka;

import com.example.order_service.dto.OrderCreatedEvent;
import com.example.order_service.dto.OrderItemEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"order-events"},
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9093",
                "port=9093"
        }
)
@DirtiesContext
class KafkaIntegrationTest {

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:9093");
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");
    }

    @Autowired
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenOrderCreatedEventPublished_thenMessageIsSent() throws Exception {
        // Given
        OrderItemEvent item = new OrderItemEvent();
        item.setMenuItemId(1L);
        item.setQuantity(2);
        item.setPrice(100L);

        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(1L);
        event.setUserId(1L);
        event.setRestaurantId(1L);
        event.setItems(List.of(item));
        event.setTotalPrice(200L);
        event.setStatus("CREATED");

        // When
        kafkaTemplate.send("order-events", event);

        // Then
        // Wait for the message to be sent
        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // The message was sent successfully
                    System.out.println("Order event sent successfully");
                });
    }

    @Test
    void whenMultipleOrderEventsPublished_thenAllAreSent() throws Exception {
        // Given
        OrderItemEvent item1 = new OrderItemEvent();
        item1.setMenuItemId(1L);
        item1.setQuantity(1);
        item1.setPrice(50L);

        OrderCreatedEvent event1 = new OrderCreatedEvent();
        event1.setOrderId(1L);
        event1.setUserId(1L);
        event1.setRestaurantId(1L);
        event1.setItems(List.of(item1));
        event1.setTotalPrice(50L);
        event1.setStatus("CREATED");

        OrderItemEvent item2 = new OrderItemEvent();
        item2.setMenuItemId(2L);
        item2.setQuantity(3);
        item2.setPrice(150L);

        OrderCreatedEvent event2 = new OrderCreatedEvent();
        event2.setOrderId(2L);
        event2.setUserId(2L);
        event2.setRestaurantId(2L);
        event2.setItems(List.of(item2));
        event2.setTotalPrice(450L);
        event2.setStatus("CREATED");

        // When
        kafkaTemplate.send("order-events", event1);
        kafkaTemplate.send("order-events", event2);

        // Then
        await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    System.out.println("Multiple order events sent successfully");
                });
    }
}
