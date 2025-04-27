package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "Task ID") @PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<Task> createTask(
            @Parameter(description = "Task object to be created") @RequestBody Task task) {
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task by ID")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @Parameter(description = "Updated task object") @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task by ID")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Task ID") @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
