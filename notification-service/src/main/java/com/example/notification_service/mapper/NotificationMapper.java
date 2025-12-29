package com.example.notification_service.mapper;

import com.example.notification_service.dto.NotificationResponse;
import com.example.notification_service.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setUserId(notification.getUserId());
        response.setNotificationType(notification.getNotificationType());
        response.setTitle(notification.getTitle());
        response.setMessage(notification.getMessage());
        response.setChannel(notification.getChannel());
        response.setStatus(notification.getStatus());
        response.setSentAt(notification.getSentAt());
        response.setCreatedAt(notification.getCreatedAt());
        response.setUpdatedAt(notification.getUpdatedAt());
        return response;
    }
}

