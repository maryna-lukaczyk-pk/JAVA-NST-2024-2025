package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
