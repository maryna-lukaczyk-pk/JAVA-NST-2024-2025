package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@Tag(
        name = "Task Management",
        description = "APIs for managing tasks"
)

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TaskController.class);


    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Operation(
            summary = "Get all tasks",
            description = "Retrieve a list of all tasks"
    )

    @GetMapping
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Operation(
            summary = "Create a new task",
            description = "Create a new task with the provided details"
    )

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

}