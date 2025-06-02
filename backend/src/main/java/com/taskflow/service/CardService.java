package com.taskflow.service;

import com.taskflow.domain.model.Card;
import com.taskflow.domain.model.TaskList;
import com.taskflow.domain.model.User;
import com.taskflow.domain.repository.CardRepository;
import com.taskflow.domain.repository.TaskListRepository;
import com.taskflow.domain.repository.UserRepository;
import com.taskflow.api.dto.CardDTO;
import com.taskflow.api.dto.CreateCardRequest;
import com.taskflow.api.dto.UpdateCardRequest;
import com.taskflow.api.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    
    private final CardRepository cardRepository;
    private final TaskListRepository taskListRepository;
    private final UserRepository userRepository;

    public CardService(CardRepository cardRepository, 
                      TaskListRepository taskListRepository,
                      UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.taskListRepository = taskListRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<CardDTO> findByTaskList(Long listId) {
        TaskList taskList = taskListRepository.findById(listId)
            .orElseThrow(() -> new RuntimeException("TaskList not found"));
        return cardRepository.findByTaskListOrderByPosition(taskList).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CardDTO findById(Long id) {
        return cardRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    @Transactional
    public CardDTO create(CreateCardRequest request) {
        TaskList taskList = taskListRepository.findById(request.getListId())
            .orElseThrow(() -> new RuntimeException("TaskList not found"));

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        Integer maxPosition = cardRepository.findMaxPositionInList(taskList);
        int newPosition = (maxPosition == null) ? 0 : maxPosition + 1;

        Card card = new Card();
        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        card.setTaskList(taskList);
        card.setAssignee(assignee);
        card.setPosition(newPosition);
        card.setDueDate(request.getDueDate());
        card.setPriority(request.getPriority());
        card.setLabels(request.getLabels());

        return toDTO(cardRepository.save(card));
    }

    @Transactional
    public CardDTO update(Long id, UpdateCardRequest request) {
        Card card = cardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Card not found"));

        if (request.getTitle() != null) {
            card.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            card.setDescription(request.getDescription());
        }
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new RuntimeException("Assignee not found"));
            card.setAssignee(assignee);
        }
        if (request.getPosition() != null) {
            card.setPosition(request.getPosition());
        }
        if (request.getDueDate() != null) {
            card.setDueDate(request.getDueDate());
        }
        if (request.getPriority() != null) {
            card.setPriority(request.getPriority());
        }
        if (request.getLabels() != null) {
            card.setLabels(request.getLabels());
        }

        return toDTO(cardRepository.save(card));
    }

    @Transactional
    public void delete(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new RuntimeException("Card not found");
        }
        cardRepository.deleteById(id);
    }

    private CardDTO toDTO(Card card) {
        CardDTO dto = new CardDTO();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setDescription(card.getDescription());
        dto.setPosition(card.getPosition());
        dto.setListId(card.getTaskList().getId());
        if (card.getAssignee() != null) {
            dto.setAssignee(toUserDTO(card.getAssignee()));
        }
        dto.setDueDate(card.getDueDate());
        dto.setPriority(card.getPriority());
        dto.setLabels(card.getLabels());
        dto.setCreatedAt(card.getCreatedAt());
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