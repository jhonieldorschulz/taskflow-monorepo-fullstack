package com.taskflow.domain.repository;

import com.taskflow.domain.model.Board;
import com.taskflow.domain.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByWorkspaceOrderByCreatedAtDesc(Workspace workspace);
    
    @Query("SELECT b FROM Board b WHERE b.workspace.id = ?1")
    List<Board> findAllByWorkspaceId(Long workspaceId);
    
    boolean existsByNameAndWorkspace(String name, Workspace workspace);
} 