package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TaskRepository extends JpaRepository<Project, Long> {
}