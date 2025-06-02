package com.taskflow.api.controller;

import com.taskflow.BaseControllerTest;
import com.taskflow.api.dto.*;
import com.taskflow.domain.model.Card.CardPriority;
import com.taskflow.service.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CardControllerTest extends BaseControllerTest {

    @MockBean
    private CardService cardService;

    @Test
    void shouldCreateCard() throws Exception {
        // given
        CreateCardRequest request = new CreateCardRequest();
        request.setTitle("Test Card");
        request.setDescription("Test Description");
        request.setListId(1L);
        request.setAssigneeId(1L);
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setPriority(CardPriority.HIGH);
        request.setLabels(Set.of("bug", "feature"));

        UserDTO assignee = new UserDTO();
        assignee.setId(1L);
        assignee.setName("Test User");

        CardDTO response = new CardDTO();
        response.setId(1L);
        response.setTitle("Test Card");
        response.setDescription("Test Description");
        response.setListId(1L);
        response.setAssignee(assignee);
        response.setPriority(CardPriority.HIGH);
        response.setLabels(Set.of("bug", "feature"));
        response.setPosition(0);

        when(cardService.create(any(CreateCardRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Card"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.listId").value(1))
                .andExpect(jsonPath("$.assignee.id").value(1))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.position").value(0));
    }

    @Test
    void shouldUpdateCard() throws Exception {
        // given
        UpdateCardRequest request = new UpdateCardRequest();
        request.setTitle("Updated Card");
        request.setDescription("Updated Description");
        request.setAssigneeId(1L);
        request.setPosition(1);
        request.setPriority(CardPriority.URGENT);
        request.setLabels(Set.of("urgent", "bug"));

        UserDTO assignee = new UserDTO();
        assignee.setId(1L);
        assignee.setName("Test User");

        CardDTO response = new CardDTO();
        response.setId(1L);
        response.setTitle("Updated Card");
        response.setDescription("Updated Description");
        response.setListId(1L);
        response.setAssignee(assignee);
        response.setPriority(CardPriority.URGENT);
        response.setLabels(Set.of("urgent", "bug"));
        response.setPosition(1);

        when(cardService.update(eq(1L), any(UpdateCardRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Card"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.assignee.id").value(1))
                .andExpect(jsonPath("$.priority").value("URGENT"))
                .andExpect(jsonPath("$.position").value(1));
    }

    @Test
    void shouldGetCardById() throws Exception {
        // given
        UserDTO assignee = new UserDTO();
        assignee.setId(1L);
        assignee.setName("Test User");

        CardDTO response = new CardDTO();
        response.setId(1L);
        response.setTitle("Test Card");
        response.setDescription("Test Description");
        response.setListId(1L);
        response.setAssignee(assignee);
        response.setPriority(CardPriority.HIGH);
        response.setPosition(0);

        when(cardService.findById(1L)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Card"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.assignee.id").value(1));
    }

    @Test
    void shouldGetAllCardsByTaskList() throws Exception {
        // given
        CardDTO card1 = new CardDTO();
        card1.setId(1L);
        card1.setTitle("Card 1");
        card1.setListId(1L);
        card1.setPosition(0);

        CardDTO card2 = new CardDTO();
        card2.setId(2L);
        card2.setTitle("Card 2");
        card2.setListId(1L);
        card2.setPosition(1);

        when(cardService.findByTaskList(1L)).thenReturn(java.util.List.of(card1, card2));

        // when & then
        mockMvc.perform(get("/api/lists/1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Card 1"))
                .andExpect(jsonPath("$[0].position").value(0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Card 2"))
                .andExpect(jsonPath("$[1].position").value(1));
    }

    @Test
    void shouldDeleteCard() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/cards/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenCreateCardWithInvalidData() throws Exception {
        // given
        CreateCardRequest request = new CreateCardRequest();
        // missing required fields

        // when & then
        mockMvc.perform(post("/api/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenCardNotFound() throws Exception {
        // given
        when(cardService.findById(999L)).thenThrow(new RuntimeException("Card not found"));

        // when & then
        mockMvc.perform(get("/api/cards/999"))
                .andExpect(status().isNotFound());
    }
} 