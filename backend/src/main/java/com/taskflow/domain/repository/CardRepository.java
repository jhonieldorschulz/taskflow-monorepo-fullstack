package com.taskflow.domain.repository;

import com.taskflow.domain.model.Card;
import com.taskflow.domain.model.TaskList;
import com.taskflow.domain.model.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByTaskListOrderByPosition(TaskList taskList);

    List<Card> findByAssigneeAndDueDateBeforeOrderByDueDate(User assignee, LocalDateTime date);

    @Query("SELECT MAX(c.position) FROM Card c WHERE c.taskList = ?1")
    Integer findMaxPositionInList(TaskList taskList);

    @Query("SELECT c FROM Card c WHERE c.taskList.board.workspace.id = ?1 AND c.assignee = ?2")
    List<Card> findAllByWorkspaceAndAssignee(Long workspaceId, User assignee);

    @Query("SELECT c FROM Card c WHERE c.dueDate < ?1 AND c.assignee = ?2")
    List<Card> findOverdueCardsByAssignee(LocalDateTime now, User assignee);
}
