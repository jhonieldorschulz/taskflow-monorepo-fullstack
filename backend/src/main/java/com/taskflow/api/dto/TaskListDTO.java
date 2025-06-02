package com.taskflow.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class TaskListDTO {
    private Long id;
    private String name;
    private Integer position;
    private Long boardId;
    private List<CardDTO> cards;
    private LocalDateTime createdAt;
}
