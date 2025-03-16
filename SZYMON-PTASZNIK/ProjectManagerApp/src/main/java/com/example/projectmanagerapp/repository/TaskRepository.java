package com.example.projectmanagerapp.repository;

import com.example.projectmanagerapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public class TaskRepository extends JpaRepository<Task, Long> {
}
