package com.example.notification_service.service;

import com.example.notification_service.dto.NotificationResponse;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.entity.NotificationChannel;
import com.example.notification_service.entity.NotificationStatus;
import com.example.notification_service.mapper.NotificationMapper;
import com.example.notification_service.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            NotificationMapper notificationMapper
    ) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    @Transactional
    public void sendOrderCreatedNotification(Long userId, Long orderId, Long totalPrice) {
        log.info("sending order created notification to user: {} for order: {}", userId, orderId);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationType("ORDER_CREATED");
        notification.setTitle("Order Created");
        notification.setMessage(String.format("Your order #%d has been created successfully. Total amount: %d KZT", orderId, totalPrice));
        notification.setChannel(NotificationChannel.EMAIL);
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(Instant.now());

        notificationRepository.save(notification);
        log.info("order created notification sent to user: {}", userId);
    }

    @Override
    @Transactional
    public void sendPaymentCompletedNotification(Long userId, Long orderId, Long amount) {
        log.info("sending payment completed notification to user: {} for order: {}", userId, orderId);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationType("PAYMENT_COMPLETED");
        notification.setTitle("Payment Completed");
        notification.setMessage(String.format("Payment of %d KZT for order #%d has been completed successfully.", amount, orderId));
        notification.setChannel(NotificationChannel.EMAIL);
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(Instant.now());

        notificationRepository.save(notification);
        log.info("payment completed notification sent to user: {}", userId);
    }

    @Override
    @Transactional
    public void sendWelcomeNotification(Long userId, String email, String firstName) {
        log.info("sending welcome notification to user: {}", userId);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setNotificationType("WELCOME");
        notification.setTitle("Welcome to QuickBite!");
        notification.setMessage(String.format("Hello %s! Welcome to QuickBite. We're excited to have you on board!", firstName));
        notification.setChannel(NotificationChannel.EMAIL);
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(Instant.now());

        notificationRepository.save(notification);
        log.info("welcome notification sent to user: {}", userId);
    }

    @Override
    public List<NotificationResponse> getNotificationsByUserId(Long userId) {
        log.info("getting notifications for user: {}", userId);
        return notificationRepository.findByUserId(userId).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getAllNotifications() {
        log.info("getting all notifications");
        return notificationRepository.findAll().stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }
}

