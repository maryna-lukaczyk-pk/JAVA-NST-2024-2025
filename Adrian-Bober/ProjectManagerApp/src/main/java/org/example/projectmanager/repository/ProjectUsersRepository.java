package org.example.projectmanager.repository;

import org.example.projectmanager.entity.ProjectUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectUsersRepository extends JpaRepository<ProjectUsers, Long> {
    List<ProjectUsers> findByProjectId(Long projectId);
    List<ProjectUsers> findByUserId(Long userId);
}