package com.taskflow.api.dto;

import com.taskflow.domain.model.Card.CardPriority;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class UpdateCardRequest {
    private String title;
    private String description;
    private Integer position;
    private Long assigneeId;
    private LocalDateTime dueDate;
    private CardPriority priority;
    private Set<String> labels;
} 