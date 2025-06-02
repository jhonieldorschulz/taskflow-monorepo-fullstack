package com.taskflow.api.controller;

import com.taskflow.BaseControllerTest;
import com.taskflow.api.dto.CreateUserRequest;
import com.taskflow.api.dto.UpdateUserRequest;
import com.taskflow.api.dto.UserDTO;
import com.taskflow.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends BaseControllerTest {

    @MockBean
    private UserService userService;

    @Test
    void shouldCreateUser() throws Exception {
        // given
        CreateUserRequest request = new CreateUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setName("Test User");

        UserDTO response = new UserDTO();
        response.setId(1L);
        response.setEmail("test@example.com");
        response.setName("Test User");

        when(userService.create(any(CreateUserRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        // given
        UpdateUserRequest request = new UpdateUserRequest();
        request.setName("Updated Name");
        request.setCurrentPassword("password123");
        request.setNewPassword("newpassword123");

        UserDTO response = new UserDTO();
        response.setId(1L);
        response.setEmail("test@example.com");
        response.setName("Updated Name");

        when(userService.update(eq(1L), any(UpdateUserRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    void shouldGetUserById() throws Exception {
        // given
        UserDTO response = new UserDTO();
        response.setId(1L);
        response.setEmail("test@example.com");
        response.setName("Test User");

        when(userService.findById(1L)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        // given
        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setName("User 1");

        UserDTO user2 = new UserDTO();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setName("User 2");

        when(userService.findAll()).thenReturn(java.util.List.of(user1, user2));

        // when & then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].email").value("user2@example.com"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenCreateUserWithInvalidData() throws Exception {
        // given
        CreateUserRequest request = new CreateUserRequest();
        // missing required fields

        // when & then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        // given
        when(userService.findById(999L)).thenThrow(new RuntimeException("User not found"));

        // when & then
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());
    }
} 