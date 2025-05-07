package com.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Operations related to managing tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    // GET: Pobierz wszystkie zadania
    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // POST: Dodaj nowe zadanie
    @PostMapping
    @Operation(summary = "Create a new task", description = "Add a new task to the system")
    public Task createTask( @Parameter(description = "Task object to be created", required = true) @RequestBody Task task) {
        return taskRepository.save(task);
    }
}