package com.taskflow.api.dto;

import com.taskflow.domain.model.Card.CardPriority;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class CardDTO {
    private Long id;
    private String title;
    private String description;
    private Integer position;
    private Long listId;
    private UserDTO assignee;
    private LocalDateTime dueDate;
    private CardPriority priority;
    private Set<String> labels;
    private LocalDateTime createdAt;
}
