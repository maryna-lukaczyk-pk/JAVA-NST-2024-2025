package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUsers_Id(Long userId);
}
