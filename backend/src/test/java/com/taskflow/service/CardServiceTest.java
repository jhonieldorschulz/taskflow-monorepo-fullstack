package com.taskflow.service;

import com.taskflow.BaseIntegrationTest;
import com.taskflow.api.dto.*;
import com.taskflow.domain.model.Card.CardPriority;
import com.taskflow.domain.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest extends BaseIntegrationTest {

    @Autowired
    private CardService cardService;

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private CardRepository cardRepository;

    private UserDTO testUser;
    private WorkspaceDTO testWorkspace;
    private BoardDTO testBoard;
    private TaskListDTO testList;

    @BeforeEach
    void setUp() {
        // Create test user
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");
        userRequest.setName("Test User");
        testUser = userService.create(userRequest);

        // Create test workspace
        CreateWorkspaceRequest workspaceRequest = new CreateWorkspaceRequest();
        workspaceRequest.setName("Test Workspace");
        workspaceRequest.setDescription("Test Description");
        testWorkspace = workspaceService.create(testUser.getId(), workspaceRequest);

        // Create test board
        CreateBoardRequest boardRequest = new CreateBoardRequest();
        boardRequest.setName("Test Board");
        boardRequest.setDescription("Test Description");
        boardRequest.setWorkspaceId(testWorkspace.getId());
        testBoard = boardService.create(boardRequest);

        // Create test list
        CreateTaskListRequest listRequest = new CreateTaskListRequest();
        listRequest.setName("Test List");
        listRequest.setBoardId(testBoard.getId());
        testList = taskListService.create(listRequest);
    }

    @Test
    void shouldCreateCard() {
        // given
        CreateCardRequest request = new CreateCardRequest();
        request.setTitle("Test Card");
        request.setDescription("Test Description");
        request.setListId(testList.getId());
        request.setAssigneeId(testUser.getId());
        request.setDueDate(LocalDateTime.now().plusDays(1));
        request.setPriority(CardPriority.HIGH);
        request.setLabels(Set.of("bug", "feature"));

        // when
        CardDTO result = cardService.create(request);

        // then
        assertNotNull(result);
        assertEquals("Test Card", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(testList.getId(), result.getListId());
        assertEquals(testUser.getId(), result.getAssignee().getId());
        assertEquals(CardPriority.HIGH, result.getPriority());
        assertTrue(result.getLabels().containsAll(Set.of("bug", "feature")));
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void shouldUpdateCard() {
        // given
        CreateCardRequest createRequest = new CreateCardRequest();
        createRequest.setTitle("Test Card");
        createRequest.setDescription("Test Description");
        createRequest.setListId(testList.getId());
        CardDTO card = cardService.create(createRequest);

        UpdateCardRequest updateRequest = new UpdateCardRequest();
        updateRequest.setTitle("Updated Card");
        updateRequest.setDescription("Updated Description");
        updateRequest.setAssigneeId(testUser.getId());
        updateRequest.setPosition(1);
        updateRequest.setPriority(CardPriority.URGENT);
        updateRequest.setLabels(Set.of("urgent", "bug"));

        // when
        CardDTO result = cardService.update(card.getId(), updateRequest);

        // then
        assertNotNull(result);
        assertEquals("Updated Card", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(testUser.getId(), result.getAssignee().getId());
        assertEquals(1, result.getPosition());
        assertEquals(CardPriority.URGENT, result.getPriority());
        assertTrue(result.getLabels().containsAll(Set.of("urgent", "bug")));
    }

    @Test
    void shouldFindAllCardsByTaskList() {
        // given
        CreateCardRequest request1 = new CreateCardRequest();
        request1.setTitle("Card 1");
        request1.setDescription("Description 1");
        request1.setListId(testList.getId());
        cardService.create(request1);

        CreateCardRequest request2 = new CreateCardRequest();
        request2.setTitle("Card 2");
        request2.setDescription("Description 2");
        request2.setListId(testList.getId());
        cardService.create(request2);

        // when
        List<CardDTO> results = cardService.findByTaskList(testList.getId());

        // then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream()
            .allMatch(card -> card.getListId().equals(testList.getId())));
    }

    @Test
    void shouldDeleteCard() {
        // given
        CreateCardRequest request = new CreateCardRequest();
        request.setTitle("Test Card");
        request.setDescription("Test Description");
        request.setListId(testList.getId());
        CardDTO card = cardService.create(request);

        // when
        cardService.delete(card.getId());

        // then
        assertFalse(cardRepository.existsById(card.getId()));
    }

    @Test
    void shouldMaintainCorrectPositionOnCreation() {
        // given
        CreateCardRequest request1 = new CreateCardRequest();
        request1.setTitle("Card 1");
        request1.setListId(testList.getId());
        CardDTO card1 = cardService.create(request1);

        CreateCardRequest request2 = new CreateCardRequest();
        request2.setTitle("Card 2");
        request2.setListId(testList.getId());
        CardDTO card2 = cardService.create(request2);

        // then
        assertEquals(0, card1.getPosition());
        assertEquals(1, card2.getPosition());
    }
} 