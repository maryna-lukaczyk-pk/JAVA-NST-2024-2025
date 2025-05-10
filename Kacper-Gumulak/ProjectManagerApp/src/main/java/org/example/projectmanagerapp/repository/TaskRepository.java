package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

// Interfejs repozytorium dla encji zadania
public interface TaskRepository extends JpaRepository<Task, Long> {
}