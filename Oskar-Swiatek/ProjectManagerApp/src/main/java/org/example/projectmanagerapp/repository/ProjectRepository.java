package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByNameContaining(String name);
}