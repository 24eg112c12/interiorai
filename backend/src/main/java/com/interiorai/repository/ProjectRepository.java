package com.interiorai.repository;

import com.interiorai.entity.Project;
import com.interiorai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUserOrderByCreatedAtDesc(User user);
    Optional<Project> findByProjectIdAndUser(Long projectId, User user);
    long countByUserAndStatus(User user, String status);
    long countByUser(User user);
}
