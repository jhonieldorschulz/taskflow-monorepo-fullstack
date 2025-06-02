package com.taskflow.service;

import com.taskflow.domain.model.Board;
import com.taskflow.domain.model.TaskList;
import com.taskflow.domain.repository.BoardRepository;
import com.taskflow.domain.repository.TaskListRepository;
import com.taskflow.api.dto.TaskListDTO;
import com.taskflow.api.dto.CreateTaskListRequest;
import com.taskflow.api.dto.UpdateTaskListRequest;
import com.taskflow.api.dto.CardDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskListService {
    
    private final TaskListRepository taskListRepository;
    private final BoardRepository boardRepository;

    public TaskListService(TaskListRepository taskListRepository, BoardRepository boardRepository) {
        this.taskListRepository = taskListRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskListDTO> findAllByBoard(Long boardId) {
        return taskListRepository.findAllByBoardIdWithCards(boardId).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskListDTO findById(Long id) {
        return taskListRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("TaskList not found"));
    }

    @Transactional
    public TaskListDTO create(CreateTaskListRequest request) {
        Board board = boardRepository.findById(request.getBoardId())
            .orElseThrow(() -> new RuntimeException("Board not found"));

        Integer maxPosition = taskListRepository.findMaxPositionInBoard(board);
        int newPosition = (maxPosition == null) ? 0 : maxPosition + 1;

        TaskList taskList = new TaskList();
        taskList.setName(request.getName());
        taskList.setBoard(board);
        taskList.setPosition(newPosition);

        return toDTO(taskListRepository.save(taskList));
    }

    @Transactional
    public TaskListDTO update(Long id, UpdateTaskListRequest request) {
        TaskList taskList = taskListRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("TaskList not found"));

        if (request.getName() != null) {
            taskList.setName(request.getName());
        }
        if (request.getPosition() != null) {
            taskList.setPosition(request.getPosition());
        }

        return toDTO(taskListRepository.save(taskList));
    }

    @Transactional
    public void delete(Long id) {
        if (!taskListRepository.existsById(id)) {
            throw new RuntimeException("TaskList not found");
        }
        taskListRepository.deleteById(id);
    }

    private TaskListDTO toDTO(TaskList taskList) {
        TaskListDTO dto = new TaskListDTO();
        dto.setId(taskList.getId());
        dto.setName(taskList.getName());
        dto.setPosition(taskList.getPosition());
        dto.setBoardId(taskList.getBoard().getId());
        dto.setCreatedAt(taskList.getCreatedAt());
        
        if (taskList.getCards() != null) {
            dto.setCards(taskList.getCards().stream()
                .map(this::toCardDTO)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private CardDTO toCardDTO(com.taskflow.domain.model.Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setDescription(card.getDescription());
        dto.setPosition(card.getPosition());
        dto.setListId(card.getTaskList().getId());
        if (card.getAssignee() != null) {
            dto.setAssignee(new UserDTO(card.getAssignee().getId(), 
                card.getAssignee().getEmail(), 
                card.getAssignee().getName(), 
                card.getAssignee().getCreatedAt()));
        }
        dto.setDueDate(card.getDueDate());
        dto.setPriority(card.getPriority());
        dto.setLabels(card.getLabels());
        dto.setCreatedAt(card.getCreatedAt());
        return dto;
    }
} 