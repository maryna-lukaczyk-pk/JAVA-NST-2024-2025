package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Projects, Long> {
}