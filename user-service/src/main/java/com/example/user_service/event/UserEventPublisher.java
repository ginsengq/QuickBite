package com.example.user_service.event;

import com.example.user_service.dto.UserCreatedEvent;

public interface UserEventPublisher {

    void publishUserCreated(UserCreatedEvent event);
}
