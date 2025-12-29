package com.example.notification_service.service;

import com.example.notification_service.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {
    void sendOrderCreatedNotification(Long userId, Long orderId, Long totalPrice);
    void sendPaymentCompletedNotification(Long userId, Long orderId, Long amount);
    void sendWelcomeNotification(Long userId, String email, String firstName);
    List<NotificationResponse> getNotificationsByUserId(Long userId);
    List<NotificationResponse> getAllNotifications();
}

