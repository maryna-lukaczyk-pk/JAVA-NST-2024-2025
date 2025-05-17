package com.example.projectmanagerapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import java.util.List;
import java.util.NoSuchElementException;

@Tag(name = "Task Controller", description = "Operations on tasks - retrieving, creating, updating, deleting information")
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAll();
    }

    @Operation(summary = "Get task by ID", description = "Retrieve a single task by its ID")
    @GetMapping("/{id}")
    public Task getTaskById(@Parameter(description = "ID of the task") @PathVariable int id) {
        return taskService.getById(id);
    }

    @Operation(summary = "Create new task", description = "Create a new task with given data")
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.create(task);
    }

    @Operation(summary = "Update task", description = "Update an existing task with given ID")
    @PutMapping("/{id}")
    public Task updateTask(
            @Parameter(description = "ID of the task to update") @PathVariable int id,
            @RequestBody Task task) {
        return taskService.update(id, task);
    }

    @Operation(summary = "Delete task", description = "Delete an existing task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@Parameter(description = "ID of the task to delete") @PathVariable int id) {
        try {
            taskService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}