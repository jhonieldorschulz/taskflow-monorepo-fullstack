package com.taskflow.service;

import com.taskflow.domain.model.Board;
import com.taskflow.domain.model.Workspace;
import com.taskflow.domain.repository.BoardRepository;
import com.taskflow.domain.repository.WorkspaceRepository;
import com.taskflow.api.dto.BoardDTO;
import com.taskflow.api.dto.CreateBoardRequest;
import com.taskflow.api.dto.UpdateBoardRequest;
import com.taskflow.api.dto.TaskListDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {
    
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;

    public BoardService(BoardRepository boardRepository, WorkspaceRepository workspaceRepository) {
        this.boardRepository = boardRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Transactional(readOnly = true)
    public List<BoardDTO> findAllByWorkspace(Long workspaceId) {
        return boardRepository.findAllByWorkspaceId(workspaceId).stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardDTO findById(Long id) {
        return boardRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RuntimeException("Board not found"));
    }

    @Transactional
    public BoardDTO create(CreateBoardRequest request) {
        Workspace workspace = workspaceRepository.findById(request.getWorkspaceId())
            .orElseThrow(() -> new RuntimeException("Workspace not found"));

        if (boardRepository.existsByNameAndWorkspace(request.getName(), workspace)) {
            throw new RuntimeException("Board with this name already exists in the workspace");
        }

        Board board = new Board();
        board.setName(request.getName());
        board.setDescription(request.getDescription());
        board.setWorkspace(workspace);

        return toDTO(boardRepository.save(board));
    }

    @Transactional
    public BoardDTO update(Long id, UpdateBoardRequest request) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Board not found"));

        if (request.getName() != null) {
            board.setName(request.getName());
        }
        if (request.getDescription() != null) {
            board.setDescription(request.getDescription());
        }

        return toDTO(boardRepository.save(board));
    }

    @Transactional
    public void delete(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new RuntimeException("Board not found");
        }
        boardRepository.deleteById(id);
    }

    private BoardDTO toDTO(Board board) {
        BoardDTO dto = new BoardDTO();
        dto.setId(board.getId());
        dto.setName(board.getName());
        dto.setDescription(board.getDescription());
        dto.setWorkspaceId(board.getWorkspace().getId());
        dto.setCreatedAt(board.getCreatedAt());
        
        if (board.getLists() != null) {
            dto.setLists(board.getLists().stream()
                .map(this::toTaskListDTO)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private TaskListDTO toTaskListDTO(com.taskflow.domain.model.TaskList taskList) {
        TaskListDTO dto = new TaskListDTO();
        dto.setId(taskList.getId());
        dto.setName(taskList.getName());
        dto.setPosition(taskList.getPosition());
        dto.setBoardId(taskList.getBoard().getId());
        dto.setCreatedAt(taskList.getCreatedAt());
        return dto;
    }
} 