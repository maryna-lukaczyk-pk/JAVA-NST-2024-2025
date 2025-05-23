package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operacje związane z zadaniami")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    @Operation(summary = "Pobierz listę wszystkich zadań", description = "Zwraca listę wszystkich zadań")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Dodaj nowe zadanie", description = "Tworzy nowe zadanie")
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }
}
