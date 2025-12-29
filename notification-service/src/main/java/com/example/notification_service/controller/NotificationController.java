package com.example.notification_service.controller;

import com.example.notification_service.dto.NotificationResponse;
import com.example.notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification", description = "Notification management APIs")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get notifications by user", description = "Returns all notifications for a specific user")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUser(@PathVariable Long userId) {
        log.info("received request to get notifications for user: {}", userId);
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all notifications", description = "Admin only - returns all notifications")
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        log.info("received request to get all notifications");
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }
}

