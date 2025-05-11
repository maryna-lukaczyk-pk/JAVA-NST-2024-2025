package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.priority.HighPriority; // Załóżmy, że masz te klasy
import com.example.projectmanagerapp.entity.priority.LowPriority;
import com.example.projectmanagerapp.entity.priority.MediumPriority;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task setTaskPriority(Long taskId, String priorityLevel) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        switch (priorityLevel.toUpperCase()) {
            case "HIGH" -> task.setPriority(new HighPriority());
            case "MEDIUM" -> task.setPriority(new MediumPriority());
            case "LOW" -> task.setPriority(new LowPriority());
            default -> {
                // Możesz rzucić wyjątek dla nieprawidłowego poziomu lub ustawić domyślny/null
                // throw new IllegalArgumentException("Invalid priority level: " + priorityLevel);
                task.setPriority(null); // lub zachowaj poprzedni
            }
        }
        return taskRepository.save(task); // Repozytorium zapisuje zaktualizowany task
    }

    // Inne metody serwisu dla Task...
}