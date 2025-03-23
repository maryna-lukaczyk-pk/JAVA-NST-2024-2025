package org.example.projectmanagerapp.repositories;
import org.example.projectmanagerapp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}