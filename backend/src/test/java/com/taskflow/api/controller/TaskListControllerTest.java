package com.taskflow.api.controller;

import com.taskflow.BaseControllerTest;
import com.taskflow.api.dto.*;
import com.taskflow.service.TaskListService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskListControllerTest extends BaseControllerTest {

    @MockBean
    private TaskListService taskListService;

    @Test
    void shouldCreateTaskList() throws Exception {
        // given
        CreateTaskListRequest request = new CreateTaskListRequest();
        request.setName("Test List");
        request.setBoardId(1L);

        TaskListDTO response = new TaskListDTO();
        response.setId(1L);
        response.setName("Test List");
        response.setBoardId(1L);
        response.setPosition(0);
        response.setCards(new ArrayList<>());

        when(taskListService.create(any(CreateTaskListRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test List"))
                .andExpect(jsonPath("$.boardId").value(1))
                .andExpect(jsonPath("$.position").value(0));
    }

    @Test
    void shouldUpdateTaskList() throws Exception {
        // given
        UpdateTaskListRequest request = new UpdateTaskListRequest();
        request.setName("Updated List");
        request.setPosition(1);

        TaskListDTO response = new TaskListDTO();
        response.setId(1L);
        response.setName("Updated List");
        response.setBoardId(1L);
        response.setPosition(1);

        when(taskListService.update(eq(1L), any(UpdateTaskListRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/lists/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated List"))
                .andExpect(jsonPath("$.position").value(1));
    }

    @Test
    void shouldGetTaskListById() throws Exception {
        // given
        TaskListDTO response = new TaskListDTO();
        response.setId(1L);
        response.setName("Test List");
        response.setBoardId(1L);
        response.setPosition(0);
        response.setCards(new ArrayList<>());

        when(taskListService.findById(1L)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/lists/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test List"))
                .andExpect(jsonPath("$.boardId").value(1));
    }

    @Test
    void shouldGetAllTaskListsByBoard() throws Exception {
        // given
        TaskListDTO list1 = new TaskListDTO();
        list1.setId(1L);
        list1.setName("List 1");
        list1.setBoardId(1L);
        list1.setPosition(0);

        TaskListDTO list2 = new TaskListDTO();
        list2.setId(2L);
        list2.setName("List 2");
        list2.setBoardId(1L);
        list2.setPosition(1);

        when(taskListService.findAllByBoard(1L)).thenReturn(java.util.List.of(list1, list2));

        // when & then
        mockMvc.perform(get("/api/boards/1/lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("List 1"))
                .andExpect(jsonPath("$[0].position").value(0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("List 2"))
                .andExpect(jsonPath("$[1].position").value(1));
    }

    @Test
    void shouldDeleteTaskList() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/lists/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenCreateTaskListWithInvalidData() throws Exception {
        // given
        CreateTaskListRequest request = new CreateTaskListRequest();
        // missing required fields

        // when & then
        mockMvc.perform(post("/api/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenTaskListNotFound() throws Exception {
        // given
        when(taskListService.findById(999L)).thenThrow(new RuntimeException("TaskList not found"));

        // when & then
        mockMvc.perform(get("/api/lists/999"))
                .andExpect(status().isNotFound());
    }
} 