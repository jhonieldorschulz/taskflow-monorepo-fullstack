package com.taskflow.service;

import com.taskflow.BaseIntegrationTest;
import com.taskflow.api.dto.CreateUserRequest;
import com.taskflow.api.dto.UpdateUserRequest;
import com.taskflow.api.dto.UserDTO;
import com.taskflow.domain.model.User;
import com.taskflow.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateUser() {
        // given
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");

        // when
        UserDTO result = userService.create(request);

        // then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test User", result.getName());
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());

        // verify password is encrypted
        User savedUser = userRepository.findById(result.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches("password123", savedUser.getPassword()));
    }

    @Test
    void shouldUpdateUser() {
        // given
        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setEmail("test@example.com");
        createRequest.setPassword("password123");
        createRequest.setName("Test User");
        UserDTO user = userService.create(createRequest);

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setName("Updated Name");
        updateRequest.setCurrentPassword("password123");
        updateRequest.setNewPassword("newpassword123");

        // when
        UserDTO result = userService.update(user.getId(), updateRequest);

        // then
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("test@example.com", result.getEmail());

        // verify password is updated and encrypted
        User savedUser = userRepository.findById(result.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches("newpassword123", savedUser.getPassword()));
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        // given
        CreateUserRequest request1 = new CreateUserRequest();
        request1.setEmail("test@example.com");
        request1.setPassword("password123");
        request1.setName("Test User 1");
        userService.create(request1);

        CreateUserRequest request2 = new CreateUserRequest();
        request2.setEmail("test@example.com");
        request2.setPassword("password456");
        request2.setName("Test User 2");

        // when & then
        assertThrows(RuntimeException.class, () -> userService.create(request2));
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsInvalid() {
        // given
        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setEmail("test@example.com");
        createRequest.setPassword("password123");
        createRequest.setName("Test User");
        UserDTO user = userService.create(createRequest);

        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setCurrentPassword("wrongpassword");
        updateRequest.setNewPassword("newpassword123");

        // when & then
        assertThrows(RuntimeException.class, () -> userService.update(user.getId(), updateRequest));
    }
} 