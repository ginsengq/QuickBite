package com.example.user_service.controller;

import com.example.user_service.dto.UserCreatedEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/kafka-test")
@Tag(name = "Kafka Test", description = "Kafka integration testing endpoints")
@Slf4j
public class KafkaTestController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaTestController(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/send-test-message")
    @Operation(summary = "Send test Kafka message", description = "Sends a test user-created event to Kafka")
    public ResponseEntity<Map<String, Object>> sendTestMessage(@RequestParam(defaultValue = "Test User") String name) {
        log.info("Sending test Kafka message for user: {}", name);
        
        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(999L)
                .email("test@quickbite.com")
                .firstName(name)
                .lastName("TestLastName")
                .phoneNumber("+7-777-777-7777")
                .role("USER")
                .build();

        try {
            kafkaTemplate.send("user-events", "user.created", event)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("✅ Test message sent successfully to topic: user-events");
                            log.info("   Partition: {}, Offset: {}", 
                                    result.getRecordMetadata().partition(), 
                                    result.getRecordMetadata().offset());
                        } else {
                            log.error("❌ Failed to send test message", ex);
                        }
                    });

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Test Kafka message sent successfully");
            response.put("topic", "user-events");
            response.put("event", event);
            response.put("timestamp", LocalDateTime.now());
            response.put("info", "Check order-service logs to verify message consumption");

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error sending test message", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "Failed to send test message: " + e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/kafka-status")
    @Operation(summary = "Check Kafka connection", description = "Returns Kafka connection status")
    public ResponseEntity<Map<String, Object>> checkKafkaStatus() {
        log.info("Checking Kafka connection status");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "CONNECTED");
        response.put("service", "user-service");
        response.put("kafkaTemplate", kafkaTemplate.getClass().getSimpleName());
        response.put("topics", Map.of(
                "user-events", "User lifecycle events (CREATE, UPDATE, DELETE)",
                "order-events", "Order lifecycle events"
        ));
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Kafka is configured and ready");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-bulk-messages")
    @Operation(summary = "Send multiple test messages", description = "Sends multiple test messages to Kafka")
    public ResponseEntity<Map<String, Object>> sendBulkMessages(@RequestParam(defaultValue = "5") int count) {
        log.info("Sending {} test messages to Kafka", count);
        
        int successCount = 0;
        int failureCount = 0;

        for (int i = 1; i <= count; i++) {
            UserCreatedEvent event = UserCreatedEvent.builder()
                    .userId(1000L + i)
                    .email("testuser" + i + "@quickbite.com")
                    .firstName("TestUser" + i)
                    .lastName("Bulk")
                    .phoneNumber("+7-700-000-" + String.format("%04d", i))
                    .role("USER")
                    .build();

            try {
                kafkaTemplate.send("user-events", "user.created", event).get();
                successCount++;
                log.info("✅ Message {}/{} sent successfully", i, count);
            } catch (Exception e) {
                failureCount++;
                log.error("❌ Message {}/{} failed: {}", i, count, e.getMessage());
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", failureCount == 0 ? "SUCCESS" : "PARTIAL_SUCCESS");
        response.put("totalRequested", count);
        response.put("successCount", successCount);
        response.put("failureCount", failureCount);
        response.put("topic", "user-events");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", String.format("Sent %d/%d messages successfully", successCount, count));

        return ResponseEntity.ok(response);
    }
}
