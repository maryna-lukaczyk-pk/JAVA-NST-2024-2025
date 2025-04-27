package org.example.projectmanagerapp.repositories;


import org.example.projectmanagerapp.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}