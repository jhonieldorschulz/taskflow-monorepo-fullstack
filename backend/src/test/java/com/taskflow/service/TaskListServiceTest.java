package com.taskflow.service;

import com.taskflow.BaseIntegrationTest;
import com.taskflow.api.dto.*;
import com.taskflow.domain.repository.TaskListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskListServiceTest extends BaseIntegrationTest {

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskListRepository taskListRepository;

    private UserDTO testUser;
    private WorkspaceDTO testWorkspace;
    private BoardDTO testBoard;

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
    }

    @Test
    void shouldCreateTaskList() {
        // given
        CreateTaskListRequest request = new CreateTaskListRequest();
        request.setName("Test List");
        request.setBoardId(testBoard.getId());

        // when
        TaskListDTO result = taskListService.create(request);

        // then
        assertNotNull(result);
        assertEquals("Test List", result.getName());
        assertEquals(testBoard.getId(), result.getBoardId());
        assertEquals(0, result.getPosition());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void shouldUpdateTaskList() {
        // given
        CreateTaskListRequest createRequest = new CreateTaskListRequest();
        createRequest.setName("Test List");
        createRequest.setBoardId(testBoard.getId());
        TaskListDTO taskList = taskListService.create(createRequest);

        UpdateTaskListRequest updateRequest = new UpdateTaskListRequest();
        updateRequest.setName("Updated List");
        updateRequest.setPosition(1);

        // when
        TaskListDTO result = taskListService.update(taskList.getId(), updateRequest);

        // then
        assertNotNull(result);
        assertEquals("Updated List", result.getName());
        assertEquals(1, result.getPosition());
        assertEquals(testBoard.getId(), result.getBoardId());
    }

    @Test
    void shouldFindAllTaskListsByBoard() {
        // given
        CreateTaskListRequest request1 = new CreateTaskListRequest();
        request1.setName("List 1");
        request1.setBoardId(testBoard.getId());
        taskListService.create(request1);

        CreateTaskListRequest request2 = new CreateTaskListRequest();
        request2.setName("List 2");
        request2.setBoardId(testBoard.getId());
        taskListService.create(request2);

        // when
        List<TaskListDTO> results = taskListService.findAllByBoard(testBoard.getId());

        // then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream()
            .allMatch(list -> list.getBoardId().equals(testBoard.getId())));
    }

    @Test
    void shouldDeleteTaskList() {
        // given
        CreateTaskListRequest request = new CreateTaskListRequest();
        request.setName("Test List");
        request.setBoardId(testBoard.getId());
        TaskListDTO taskList = taskListService.create(request);

        // when
        taskListService.delete(taskList.getId());

        // then
        assertFalse(taskListRepository.existsById(taskList.getId()));
    }

    @Test
    void shouldMaintainCorrectPositionOnCreation() {
        // given
        CreateTaskListRequest request1 = new CreateTaskListRequest();
        request1.setName("List 1");
        request1.setBoardId(testBoard.getId());
        TaskListDTO list1 = taskListService.create(request1);

        CreateTaskListRequest request2 = new CreateTaskListRequest();
        request2.setName("List 2");
        request2.setBoardId(testBoard.getId());
        TaskListDTO list2 = taskListService.create(request2);

        // then
        assertEquals(0, list1.getPosition());
        assertEquals(1, list2.getPosition());
    }
} 