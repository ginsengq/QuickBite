package com.example.user_service.mapper;

import com.example.user_service.dto.CreateUserRequest;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRole(request.getRole());
        user.setActive(true);
        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                user.getActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
