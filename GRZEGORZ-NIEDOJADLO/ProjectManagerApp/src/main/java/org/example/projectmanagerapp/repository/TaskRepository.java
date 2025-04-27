package org.example.projectmanagerapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.projectmanagerapp.entity.task.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
