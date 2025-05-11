package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repozytorium.ProjectRepository;
import com.example.projectmanagerapp.repozytorium.TasksRepository;
import com.example.projectmanagerapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "API for managing tasks.")
public class TasksController {
    private final TaskService taskService;

    @Autowired
    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find task by ID",
            description = "Returns a task matching the provided ID."
    )
    public ResponseEntity<Tasks> getTaskById(
            @Parameter(description = "ID of the task to be retrieved") @PathVariable Long id) {
        try{
            Tasks task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        }catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(
            summary = "Create a new task",
            description = "Creates a new task and returns the created entity."
    )
    public ResponseEntity<Tasks> createTask(
            @Parameter(description = "Details of the task to be created") @RequestBody Tasks tasks) {
        return new ResponseEntity<>(taskService.createTask(tasks), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing task",
            description = "Updates the task matching the provided ID with new data."
    )
    public ResponseEntity<Tasks> updateTask(
            @Parameter(description = "ID of the task to be updated") @PathVariable Long id,
            @Parameter(description = "Updated task data") @RequestBody Tasks taskDetails) {
        try {
            Tasks updatedTask = taskService.updateTask(id, taskDetails);
            return ResponseEntity.ok(updatedTask);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a task",
            description = "Deletes the task matching the provided ID."
    )
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to be deleted") @PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

