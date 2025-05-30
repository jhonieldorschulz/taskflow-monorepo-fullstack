package com.taskflow.domain.repository;

import com.taskflow.domain.model.User;
import com.taskflow.domain.model.Workspace;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    List<Workspace> findByOwner(User owner);

    @Query("SELECT w FROM Workspace w WHERE w.owner = ?1 OR ?1 MEMBER OF w.members")
    List<Workspace> findAllAccessibleByUser(User user);

    boolean existsByNameAndOwner(String name, User owner);
}
