package com.example.projectmanagerapp.controllers;


import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Task", description = "Methods of Task")
public class TaskController {
    private final TaskRepository repository;

    TaskController(TaskRepository repository) {
        this.repository= repository;
    }

    @Operation(summary = "Get all tasks")
    @GetMapping("/tasks")
    List<Task> all() {
        return repository.findAll();
    }

    @Operation(summary = "Add a task")
    @PostMapping("/tasks")
    Task newTask(@RequestBody Task newTask) {
        return repository.save(newTask);
    }
}
