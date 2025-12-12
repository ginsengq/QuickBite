package com.example.order_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
@RequestMapping("/api/kafka-test")
@Tag(name = "Kafka Test", description = "Kafka consumer testing endpoints")
@Slf4j
public class KafkaTestController {

    // In-memory storage for received messages (for testing purposes)
    private static final ConcurrentLinkedQueue<Map<String, Object>> receivedMessages = new ConcurrentLinkedQueue<>();
    private static int totalReceivedCount = 0;

    public static void recordReceivedMessage(String topic, Object payload) {
        totalReceivedCount++;
        
        Map<String, Object> message = new HashMap<>();
        message.put("messageNumber", totalReceivedCount);
        message.put("topic", topic);
        message.put("payload", payload);
        message.put("receivedAt", LocalDateTime.now());
        
        receivedMessages.add(message);
        
        // Keep only last 50 messages
        while (receivedMessages.size() > 50) {
            receivedMessages.poll();
        }
    }

    @GetMapping("/received-messages")
    @Operation(summary = "Get received Kafka messages", description = "Returns list of recently received Kafka messages")
    public ResponseEntity<Map<String, Object>> getReceivedMessages() {
        log.info("Fetching received Kafka messages");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("totalReceived", totalReceivedCount);
        response.put("messagesInMemory", receivedMessages.size());
        response.put("messages", new ArrayList<>(receivedMessages));
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consumer-status")
    @Operation(summary = "Check Kafka consumer status", description = "Returns Kafka consumer status and statistics")
    public ResponseEntity<Map<String, Object>> getConsumerStatus() {
        log.info("Checking Kafka consumer status");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "LISTENING");
        response.put("service", "order-service");
        response.put("topics", List.of("user-events", "order-events"));
        response.put("groupId", "order-service-group");
        response.put("totalMessagesReceived", totalReceivedCount);
        response.put("messagesInMemory", receivedMessages.size());
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Kafka consumer is active and listening");
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear-messages")
    @Operation(summary = "Clear received messages", description = "Clears the in-memory message storage")
    public ResponseEntity<Map<String, Object>> clearMessages() {
        log.info("Clearing received messages");
        
        int clearedCount = receivedMessages.size();
        receivedMessages.clear();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Received messages cleared");
        response.put("clearedCount", clearedCount);
        response.put("totalReceivedCount", totalReceivedCount);
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-counter")
    @Operation(summary = "Reset message counter", description = "Resets the total received message counter")
    public ResponseEntity<Map<String, Object>> resetCounter() {
        log.info("Resetting message counter");
        
        int previousCount = totalReceivedCount;
        totalReceivedCount = 0;
        receivedMessages.clear();
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Counter and messages reset");
        response.put("previousCount", previousCount);
        response.put("currentCount", totalReceivedCount);
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }
}
