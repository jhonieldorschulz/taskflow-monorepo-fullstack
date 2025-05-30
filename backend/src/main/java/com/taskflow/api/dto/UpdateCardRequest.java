package com.taskflow.api.dto;

import com.taskflow.domain.model.Card.CardPriority;
import java.time.LocalDateTime;
import java.util.Set;

public class UpdateCardRequest {

    private String title;
    private String description;
    private Long assigneeId;
    private LocalDateTime dueDate;
    private CardPriority priority;
    private Set<String> labels;
    private Integer position;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public CardPriority getPriority() {
        return priority;
    }

    public void setPriority(CardPriority priority) {
        this.priority = priority;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
