package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.priority.LowPriority;
import org.example.projectmanagerapp.priority.MediumPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operacje -zadania")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/add")
    @Operation(summary = "Dodaj nowe zadanie", description = "Tworzy nowe zadanie")
    public Task createTask(@Parameter(description = "Powstaje nowe zadanie") @RequestParam String name, @RequestParam String priority) {
        PriorityLevel priorityLevel;

        switch (priority.toUpperCase()) {
            case "HIGH":
                priorityLevel = new HighPriority();
                break;
            case "MEDIUM":
                priorityLevel = new MediumPriority();
                break;
            case "LOW":
            default:
                priorityLevel = new LowPriority();
                break;
        }

        Task task = new Task(name, priorityLevel);
        return taskRepository.save(task);
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkie zadania", description = "Zwraca listę zadań")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
