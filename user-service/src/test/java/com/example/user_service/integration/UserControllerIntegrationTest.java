package com.example.user_service.integration;

import com.example.user_service.dto.CreateUserRequest;
import com.example.user_service.dto.UserResponse;
import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class UserControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:29092");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_Success() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("integration@test.com");
        request.setFirstName("Integration");
        request.setLastName("Test");
        request.setPhone("+1234567890");
        request.setAddress("123 Test St");
        request.setRole("USER");

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("integration@test.com"))
                .andExpect(jsonPath("$.firstName").value("Integration"))
                .andExpect(jsonPath("$.role").value("USER"));

        // Verify in database
        assertThat(userRepository.findByEmail("integration@test.com")).isPresent();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_Success() throws Exception {
        // Given
        User user = new User();
        user.setEmail("existing@test.com");
        user.setFirstName("Existing");
        user.setLastName("User");
        user.setPhone("+0987654321");
        user.setAddress("456 Test Ave");
        user.setRole("USER");
        user.setActive(true);
        User savedUser = userRepository.save(user);

        // When & Then
        mockMvc.perform(get("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.email").value("existing@test.com"))
                .andExpect(jsonPath("$.firstName").value("Existing"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_NotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_Success() throws Exception {
        // Given
        User user1 = new User();
        user1.setEmail("user1@test.com");
        user1.setFirstName("User");
        user1.setLastName("One");
        user1.setPhone("+1111111111");
        user1.setAddress("111 Test St");
        user1.setRole("USER");
        user1.setActive(true);
        userRepository.save(user1);

        User user2 = new User();
        user2.setEmail("user2@test.com");
        user2.setFirstName("User");
        user2.setLastName("Two");
        user2.setPhone("+2222222222");
        user2.setAddress("222 Test Ave");
        user2.setRole("ADMIN");
        user2.setActive(true);
        userRepository.save(user2);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[1].email").exists());
    }
}
