package com.taskflow.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String name;
    private String description;
    private Long workspaceId;
    private List<TaskListDTO> lists;
    private LocalDateTime createdAt;
}
