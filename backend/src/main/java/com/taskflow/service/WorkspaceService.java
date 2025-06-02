package com.taskflow.service;

import com.taskflow.domain.model.User;
import com.taskflow.domain.model.Workspace;
import com.taskflow.domain.repository.UserRepository;
import com.taskflow.domain.repository.WorkspaceRepository;
import com.taskflow.api.dto.CreateWorkspaceRequest;
import com.taskflow.api.dto.UpdateWorkspaceRequest;
import com.taskflow.api.dto.WorkspaceDTO;
import com.taskflow.api.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkspaceService {
    
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository, UserRepository userRepository) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<WorkspaceDTO> findAllByUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return workspaceRepository.findAllAccessibleByUser(user).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkspaceDTO findById(Long id) {
        return workspaceRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Workspace not found"));
    }

    @Transactional
    public WorkspaceDTO create(Long userId, CreateWorkspaceRequest request) {
        User owner = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (workspaceRepository.existsByNameAndOwner(request.getName(), owner)) {
            throw new RuntimeException("Workspace with this name already exists");
        }

        Workspace workspace = new Workspace();
        workspace.setName(request.getName());
        workspace.setDescription(request.getDescription());
        workspace.setOwner(owner);
        workspace.setMembers(new HashSet<>());
        workspace.getMembers().add(owner);

        return toDTO(workspaceRepository.save(workspace));
    }

    @Transactional
    public WorkspaceDTO update(Long id, UpdateWorkspaceRequest request) {
        Workspace workspace = workspaceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Workspace not found"));

        if (request.getName() != null) {
            workspace.setName(request.getName());
        }
        if (request.getDescription() != null) {
            workspace.setDescription(request.getDescription());
        }
        if (request.getMemberIds() != null) {
            Set<User> members = request.getMemberIds().stream()
                .map(memberId -> userRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Member not found: " + memberId)))
                .collect(Collectors.toSet());
            workspace.setMembers(members);
            workspace.getMembers().add(workspace.getOwner()); // Ensure owner remains a member
        }

        return toDTO(workspaceRepository.save(workspace));
    }

    @Transactional
    public void delete(Long id) {
        if (!workspaceRepository.existsById(id)) {
            throw new RuntimeException("Workspace not found");
        }
        workspaceRepository.deleteById(id);
    }

    private WorkspaceDTO toDTO(Workspace workspace) {
        WorkspaceDTO dto = new WorkspaceDTO();
        dto.setId(workspace.getId());
        dto.setName(workspace.getName());
        dto.setDescription(workspace.getDescription());
        dto.setOwner(toUserDTO(workspace.getOwner()));
        dto.setMembers(workspace.getMembers().stream()
            .map(this::toUserDTO)
            .collect(Collectors.toSet()));
        dto.setCreatedAt(workspace.getCreatedAt());
        return dto;
    }

    private UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
} 