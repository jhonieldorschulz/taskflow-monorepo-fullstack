package com.taskflow.api.controller;

import com.taskflow.BaseControllerTest;
import com.taskflow.api.dto.*;
import com.taskflow.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BoardControllerTest extends BaseControllerTest {

    @MockBean
    private BoardService boardService;

    @Test
    void shouldCreateBoard() throws Exception {
        // given
        CreateBoardRequest request = new CreateBoardRequest();
        request.setName("Test Board");
        request.setDescription("Test Description");
        request.setWorkspaceId(1L);

        BoardDTO response = new BoardDTO();
        response.setId(1L);
        response.setName("Test Board");
        response.setDescription("Test Description");
        response.setWorkspaceId(1L);
        response.setLists(new ArrayList<>());

        when(boardService.create(any(CreateBoardRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Board"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.workspaceId").value(1));
    }

    @Test
    void shouldUpdateBoard() throws Exception {
        // given
        UpdateBoardRequest request = new UpdateBoardRequest();
        request.setName("Updated Board");
        request.setDescription("Updated Description");

        BoardDTO response = new BoardDTO();
        response.setId(1L);
        response.setName("Updated Board");
        response.setDescription("Updated Description");
        response.setWorkspaceId(1L);

        when(boardService.update(eq(1L), any(UpdateBoardRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/boards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Board"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    void shouldGetBoardById() throws Exception {
        // given
        BoardDTO response = new BoardDTO();
        response.setId(1L);
        response.setName("Test Board");
        response.setDescription("Test Description");
        response.setWorkspaceId(1L);
        response.setLists(new ArrayList<>());

        when(boardService.findById(1L)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/boards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Board"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void shouldGetAllBoardsByWorkspace() throws Exception {
        // given
        BoardDTO board1 = new BoardDTO();
        board1.setId(1L);
        board1.setName("Board 1");
        board1.setWorkspaceId(1L);

        BoardDTO board2 = new BoardDTO();
        board2.setId(2L);
        board2.setName("Board 2");
        board2.setWorkspaceId(1L);

        when(boardService.findAllByWorkspace(1L)).thenReturn(java.util.List.of(board1, board2));

        // when & then
        mockMvc.perform(get("/api/workspaces/1/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Board 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Board 2"));
    }

    @Test
    void shouldDeleteBoard() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/boards/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenCreateBoardWithInvalidData() throws Exception {
        // given
        CreateBoardRequest request = new CreateBoardRequest();
        // missing required fields

        // when & then
        mockMvc.perform(post("/api/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenBoardNotFound() throws Exception {
        // given
        when(boardService.findById(999L)).thenThrow(new RuntimeException("Board not found"));

        // when & then
        mockMvc.perform(get("/api/boards/999"))
                .andExpect(status().isNotFound());
    }
} 