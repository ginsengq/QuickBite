package com.example.user_service.service;

import com.example.user_service.dto.CreateUserRequest;
import com.example.user_service.dto.UpdateUserRequest;
import com.example.user_service.dto.UserCreatedEvent;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.entity.User;
import com.example.user_service.event.UserEventPublisher;
import com.example.user_service.exception.UserAlreadyExistsException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserEventPublisher eventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private CreateUserRequest createRequest;
    private UpdateUserRequest updateRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("+1234567890");
        user.setAddress("123 Main St");
        user.setRole("CUSTOMER");
        user.setActive(true);

        createRequest = new CreateUserRequest();
        createRequest.setEmail("test@example.com");
        createRequest.setFirstName("John");
        createRequest.setLastName("Doe");
        createRequest.setPhone("+1234567890");
        createRequest.setAddress("123 Main St");
        createRequest.setRole("CUSTOMER");

        updateRequest = new UpdateUserRequest();
        updateRequest.setFirstName("Jane");
        updateRequest.setLastName("Smith");
        updateRequest.setPhone("+0987654321");
        updateRequest.setAddress("456 Oak Ave");
        updateRequest.setActive(true);

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("test@example.com");
        userResponse.setFirstName("John");
        userResponse.setLastName("Doe");
        userResponse.setPhone("+1234567890");
        userResponse.setAddress("123 Main St");
        userResponse.setRole("CUSTOMER");
        userResponse.setActive(true);
    }

    @Test
    void createUser_Success() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.toEntity(any(CreateUserRequest.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);
        doNothing().when(eventPublisher).publishUserCreated(any(UserCreatedEvent.class));

        // When
        UserResponse result = userService.createUser(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(eventPublisher, times(1)).publishUserCreated(any(UserCreatedEvent.class));
    }

    @Test
    void createUser_EmailAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(createRequest))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("test@example.com");

        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
        verify(eventPublisher, never()).publishUserCreated(any(UserCreatedEvent.class));
    }

    @Test
    void getUserById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        // When
        UserResponse result = userService.getUserById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("1");

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void updateUser_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        // When
        UserResponse result = userService.updateUser(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllUsers_Success() {
        // Given
        List<User> users = Arrays.asList(user, user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        // When
        List<UserResponse> result = userService.getAllUsers();

        // Then
        assertThat(result).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }
}
