package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    @GetMapping
    public List<Tasks> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Get task by ID", description = "Retrieve a specific task by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Tasks> getTaskById(
            @Parameter(description = "ID of the task to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        Optional<Tasks> taskOptional = taskService.getTaskById(id);
        return taskOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new task", description = "Create a new task with provided details")
    @PostMapping
    public ResponseEntity<Tasks> createTask(
            @Parameter(description = "Task object to create. Title is typically required.", required = true)
            @RequestBody Tasks task) {
        Tasks createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Update task", description = "Update an existing task by its ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<Tasks> updateTask(
            @Parameter(description = "ID of the task to update", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Task object with updated information.", required = true)
            @RequestBody Tasks taskDetails) {
        try {
            Tasks updatedTask = taskService.updateTask(id, taskDetails);
            if (updatedTask != null) {
                return ResponseEntity.ok(updatedTask);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete task", description = "Delete a task by its ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete", required = true, example = "1")
            @PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
