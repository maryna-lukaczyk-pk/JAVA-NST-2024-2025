package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

// Interfejs repozytorium dla encji projektu
public interface ProjectRepository extends JpaRepository<Project, Long> {
}