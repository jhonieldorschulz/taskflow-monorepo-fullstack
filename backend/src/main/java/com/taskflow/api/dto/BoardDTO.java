package com.taskflow.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BoardDTO {

    private Long id;
    private String name;
    private String description;
    private Long workspaceId;
    private List<TaskListDTO> lists;
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public List<TaskListDTO> getLists() {
        return lists;
    }

    public void setLists(List<TaskListDTO> lists) {
        this.lists = lists;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
