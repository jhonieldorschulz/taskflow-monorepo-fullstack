package com.taskflow.service;

import com.taskflow.BaseIntegrationTest;
import com.taskflow.api.dto.*;
import com.taskflow.domain.model.User;
import com.taskflow.domain.repository.UserRepository;
import com.taskflow.domain.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WorkspaceServiceTest extends BaseIntegrationTest {

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserRepository userRepository;

    private UserDTO testUser;

    @BeforeEach
    void setUp() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("password123");
        userRequest.setName("Test User");
        testUser = userService.create(userRequest);
    }

    @Test
    void shouldCreateWorkspace() {
        // given
        CreateWorkspaceRequest request = new CreateWorkspaceRequest();
        request.setName("Test Workspace");
        request.setDescription("Test Description");

        // when
        WorkspaceDTO result = workspaceService.create(testUser.getId(), request);

        // then
        assertNotNull(result);
        assertEquals("Test Workspace", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertEquals(testUser.getId(), result.getOwner().getId());
        assertTrue(result.getMembers().stream()
            .anyMatch(member -> member.getId().equals(testUser.getId())));
    }

    @Test
    void shouldUpdateWorkspace() {
        // given
        CreateWorkspaceRequest createRequest = new CreateWorkspaceRequest();
        createRequest.setName("Test Workspace");
        createRequest.setDescription("Test Description");
        WorkspaceDTO workspace = workspaceService.create(testUser.getId(), createRequest);

        // Create another user to add as member
        CreateUserRequest memberRequest = new CreateUserRequest();
        memberRequest.setEmail("member@example.com");
        memberRequest.setPassword("password123");
        memberRequest.setName("Test Member");
        UserDTO member = userService.create(memberRequest);

        UpdateWorkspaceRequest updateRequest = new UpdateWorkspaceRequest();
        updateRequest.setName("Updated Workspace");
        updateRequest.setDescription("Updated Description");
        updateRequest.setMemberIds(Set.of(testUser.getId(), member.getId()));

        // when
        WorkspaceDTO result = workspaceService.update(workspace.getId(), updateRequest);

        // then
        assertNotNull(result);
        assertEquals("Updated Workspace", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(2, result.getMembers().size());
        assertTrue(result.getMembers().stream()
            .anyMatch(m -> m.getId().equals(member.getId())));
    }

    @Test
    void shouldFindAllWorkspacesByUser() {
        // given
        CreateWorkspaceRequest request1 = new CreateWorkspaceRequest();
        request1.setName("Workspace 1");
        request1.setDescription("Description 1");
        workspaceService.create(testUser.getId(), request1);

        CreateWorkspaceRequest request2 = new CreateWorkspaceRequest();
        request2.setName("Workspace 2");
        request2.setDescription("Description 2");
        workspaceService.create(testUser.getId(), request2);

        // when
        List<WorkspaceDTO> results = workspaceService.findAllByUser(testUser.getId());

        // then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream()
            .allMatch(workspace -> workspace.getOwner().getId().equals(testUser.getId())));
    }

    @Test
    void shouldThrowExceptionWhenWorkspaceNameExists() {
        // given
        CreateWorkspaceRequest request1 = new CreateWorkspaceRequest();
        request1.setName("Test Workspace");
        request1.setDescription("Description 1");
        workspaceService.create(testUser.getId(), request1);

        CreateWorkspaceRequest request2 = new CreateWorkspaceRequest();
        request2.setName("Test Workspace");
        request2.setDescription("Description 2");

        // when & then
        assertThrows(RuntimeException.class, () -> workspaceService.create(testUser.getId(), request2));
    }
} 