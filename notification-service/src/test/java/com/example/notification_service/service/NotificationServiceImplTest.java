package com.example.notification_service.service;

import com.example.notification_service.dto.NotificationResponse;
import com.example.notification_service.entity.Notification;
import com.example.notification_service.entity.NotificationChannel;
import com.example.notification_service.entity.NotificationStatus;
import com.example.notification_service.mapper.NotificationMapper;
import com.example.notification_service.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification notification;
    private NotificationResponse notificationResponse;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setId(1L);
        notification.setUserId(1L);
        notification.setNotificationType("ORDER_CREATED");
        notification.setTitle("Order Created");
        notification.setMessage("Your order has been created successfully");
        notification.setChannel(NotificationChannel.EMAIL);
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(Instant.now());

        notificationResponse = new NotificationResponse();
        notificationResponse.setId(1L);
        notificationResponse.setUserId(1L);
        notificationResponse.setNotificationType("ORDER_CREATED");
        notificationResponse.setTitle("Order Created");
        notificationResponse.setMessage("Your order has been created successfully");
    }

    @Test
    void sendOrderCreatedNotification_Success() {
        // Arrange
        Long userId = 1L;
        Long orderId = 100L;
        Long totalPrice = 10000L;
        
        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        notificationService.sendOrderCreatedNotification(userId, orderId, totalPrice);

        // Assert
        verify(notificationRepository).save(notificationCaptor.capture());
        Notification savedNotification = notificationCaptor.getValue();
        assertEquals(userId, savedNotification.getUserId());
        assertEquals("ORDER_CREATED", savedNotification.getNotificationType());
        assertEquals(NotificationChannel.EMAIL, savedNotification.getChannel());
        assertEquals(NotificationStatus.SENT, savedNotification.getStatus());
        assertNotNull(savedNotification.getSentAt());
    }

    @Test
    void sendPaymentCompletedNotification_Success() {
        // Arrange
        Long userId = 1L;
        Long orderId = 100L;
        Long amount = 10000L;
        
        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        notificationService.sendPaymentCompletedNotification(userId, orderId, amount);

        // Assert
        verify(notificationRepository).save(notificationCaptor.capture());
        Notification savedNotification = notificationCaptor.getValue();
        assertEquals(userId, savedNotification.getUserId());
        assertEquals("PAYMENT_COMPLETED", savedNotification.getNotificationType());
        assertEquals(NotificationChannel.EMAIL, savedNotification.getChannel());
        assertEquals(NotificationStatus.SENT, savedNotification.getStatus());
    }

    @Test
    void sendWelcomeNotification_Success() {
        // Arrange
        Long userId = 1L;
        String email = "test@example.com";
        String firstName = "John";
        
        ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        notificationService.sendWelcomeNotification(userId, email, firstName);

        // Assert
        verify(notificationRepository).save(notificationCaptor.capture());
        Notification savedNotification = notificationCaptor.getValue();
        assertEquals(userId, savedNotification.getUserId());
        assertEquals("WELCOME", savedNotification.getNotificationType());
        assertTrue(savedNotification.getMessage().contains(firstName));
        assertEquals(NotificationChannel.EMAIL, savedNotification.getChannel());
        assertEquals(NotificationStatus.SENT, savedNotification.getStatus());
    }

    @Test
    void getNotificationsByUserId_Success() {
        // Arrange
        List<Notification> notifications = Arrays.asList(notification);
        when(notificationRepository.findByUserId(1L)).thenReturn(notifications);
        when(notificationMapper.toResponse(any(Notification.class))).thenReturn(notificationResponse);

        // Act
        List<NotificationResponse> result = notificationService.getNotificationsByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(notificationRepository).findByUserId(1L);
    }

    @Test
    void getAllNotifications_Success() {
        // Arrange
        List<Notification> notifications = Arrays.asList(notification);
        when(notificationRepository.findAll()).thenReturn(notifications);
        when(notificationMapper.toResponse(any(Notification.class))).thenReturn(notificationResponse);

        // Act
        List<NotificationResponse> result = notificationService.getAllNotifications();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(notificationRepository).findAll();
    }
}
