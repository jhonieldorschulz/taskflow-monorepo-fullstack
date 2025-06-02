package com.taskflow.service;

import com.taskflow.BaseIntegrationTest;
import com.taskflow.api.dto.*;
import com.taskflow.domain.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest extends BaseIntegrationTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private BoardRepository boardRepository;

    private UserDTO testUser;
    private WorkspaceDTO testWorkspace;

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
    }

    @Test
    void shouldCreateBoard() {
        // given
        CreateBoardRequest request = new CreateBoardRequest();
        request.setName("Test Board");
        request.setDescription("Test Description");
        request.setWorkspaceId(testWorkspace.getId());

        // when
        BoardDTO result = boardService.create(request);

        // then
        assertNotNull(result);
        assertEquals("Test Board", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(testWorkspace.getId(), result.getWorkspaceId());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void shouldUpdateBoard() {
        // given
        CreateBoardRequest createRequest = new CreateBoardRequest();
        createRequest.setName("Test Board");
        createRequest.setDescription("Test Description");
        createRequest.setWorkspaceId(testWorkspace.getId());
        BoardDTO board = boardService.create(createRequest);

        UpdateBoardRequest updateRequest = new UpdateBoardRequest();
        updateRequest.setName("Updated Board");
        updateRequest.setDescription("Updated Description");

        // when
        BoardDTO result = boardService.update(board.getId(), updateRequest);

        // then
        assertNotNull(result);
        assertEquals("Updated Board", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(testWorkspace.getId(), result.getWorkspaceId());
    }

    @Test
    void shouldFindAllBoardsByWorkspace() {
        // given
        CreateBoardRequest request1 = new CreateBoardRequest();
        request1.setName("Board 1");
        request1.setDescription("Description 1");
        request1.setWorkspaceId(testWorkspace.getId());
        boardService.create(request1);

        CreateBoardRequest request2 = new CreateBoardRequest();
        request2.setName("Board 2");
        request2.setDescription("Description 2");
        request2.setWorkspaceId(testWorkspace.getId());
        boardService.create(request2);

        // when
        List<BoardDTO> results = boardService.findAllByWorkspace(testWorkspace.getId());

        // then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream()
            .allMatch(board -> board.getWorkspaceId().equals(testWorkspace.getId())));
    }

    @Test
    void shouldThrowExceptionWhenBoardNameExists() {
        // given
        CreateBoardRequest request1 = new CreateBoardRequest();
        request1.setName("Test Board");
        request1.setDescription("Description 1");
        request1.setWorkspaceId(testWorkspace.getId());
        boardService.create(request1);

        CreateBoardRequest request2 = new CreateBoardRequest();
        request2.setName("Test Board");
        request2.setDescription("Description 2");
        request2.setWorkspaceId(testWorkspace.getId());

        // when & then
        assertThrows(RuntimeException.class, () -> boardService.create(request2));
    }

    @Test
    void shouldDeleteBoard() {
        // given
        CreateBoardRequest request = new CreateBoardRequest();
        request.setName("Test Board");
        request.setDescription("Test Description");
        request.setWorkspaceId(testWorkspace.getId());
        BoardDTO board = boardService.create(request);

        // when
        boardService.delete(board.getId());

        // then
        assertFalse(boardRepository.existsById(board.getId()));
    }
} 