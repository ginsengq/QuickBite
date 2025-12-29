package com.example.notification_service.dto;

import com.example.notification_service.entity.NotificationChannel;
import com.example.notification_service.entity.NotificationStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String notificationType;
    private String title;
    private String message;
    private NotificationChannel channel;
    private NotificationStatus status;
    private Instant sentAt;
    private Instant createdAt;
    private Instant updatedAt;
}

