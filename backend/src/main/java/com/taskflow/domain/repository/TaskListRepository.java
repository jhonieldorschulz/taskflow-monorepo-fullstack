package com.taskflow.domain.repository;

import com.taskflow.domain.model.Board;
import com.taskflow.domain.model.TaskList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findByBoardOrderByPosition(Board board);

    @Query("SELECT MAX(t.position) FROM TaskList t WHERE t.board = ?1")
    Integer findMaxPositionInBoard(Board board);

    @Query("SELECT t FROM TaskList t LEFT JOIN FETCH t.cards WHERE t.board.id = ?1 ORDER BY t.position")
    List<TaskList> findAllByBoardIdWithCards(Long boardId);
}
