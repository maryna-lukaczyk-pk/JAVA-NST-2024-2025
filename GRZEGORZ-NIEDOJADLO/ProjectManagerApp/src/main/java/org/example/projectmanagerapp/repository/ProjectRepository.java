package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}