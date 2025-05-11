package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.tasks;
import com.example.projectmanagerapp.service.tasks_service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Tasks Controller")
@RestController
@RequestMapping("/api/tasks")
public class tasks_controller {

    @Autowired
    private tasks_service tasks_service;

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Returns all tasks")
    public List<tasks> getAllTasks() {
        return tasks_service.getAllTasks();
    }

    @PostMapping("/")
    @Operation(summary = "Creating a task", description = "Saving a task")
    public tasks createTask(@RequestBody tasks task) {
        return tasks_service.create_task(task);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find task by ID", description = "Returns a single task by ID")
    public ResponseEntity<tasks> getTaskById(
            @Parameter(description = "ID of the task to retrieve") @PathVariable Long id) {
        Optional<tasks> task = tasks_service.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Updates task information by ID")
    public ResponseEntity<tasks> updateTask(
            @Parameter(description = "ID of the task to update") @PathVariable Long id,
            @RequestBody tasks task) {
        tasks updatedTask = tasks_service.updateTask(id, task);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Deletes a task by ID")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete") @PathVariable Long id) {
        tasks_service.deleteTaskById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}