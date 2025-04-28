package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.tasks;
import com.example.projectmanagerapp.service.tasks_service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    @Operation(summary = "Creating a task", description = "Saving a task")
    public tasks createTask(
            @Parameter(description = "Task object that needs to be saved", required = true)
            @RequestBody tasks task) {
        return tasks_service.create_task(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a task", description = "Updates a task by its ID")
    public tasks updateTask(
            @Parameter(description = "ID of the task to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated task object", required = true)
            @RequestBody tasks task) {
        return tasks_service.update_task(id, task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a task", description = "Deletes a task by its ID")
    public void deleteTask(
            @Parameter(description = "ID of the task to delete", required = true)
            @PathVariable Long id) {
        tasks_service.delete_task(id);
    }
}