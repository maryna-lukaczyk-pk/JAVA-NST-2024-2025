package com.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operations for managing tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks")
    public ResponseEntity<List<Task>> getTasks() {
        List<Task> tasks = taskService.getTasks();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new task", description = "Creates a new task in the database")
    public ResponseEntity<Task> create(
            @Parameter(description = "Task object to create", required = true)
            @RequestBody Task task) {
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Updates an existing task by ID")
    public ResponseEntity<Task> update(
            @Parameter(description = "ID of the task to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated task object", required = true)
            @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a task by ID", description = "Fetches a single task by its ID")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "ID of the task to retrieve", required = true)
            @PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Deletes a task by ID")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the task to delete", required = true)
            @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
