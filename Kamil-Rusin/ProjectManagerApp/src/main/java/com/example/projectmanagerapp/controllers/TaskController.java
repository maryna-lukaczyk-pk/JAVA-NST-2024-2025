package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Tag(name = "Tasks", description = "Operations to manage tasks")
@RestController
@RequestMapping("api/tasks")
public class TaskController {
    public final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Retrieve all tasks")
    @GetMapping("/all")
    public List<Task> getAll() {
        return taskService.getAll();
    }

    @Operation(summary = "Retrieve task by Id")
    @GetMapping("/{id}")
    public Optional<Task> getTaskById(
            @Parameter(description = "Id of the task to retrieve", example = "5")
            @PathVariable("id") Long id) {
        return taskService.getTaskById(id);
    }

    @Operation(summary = "Save task to database")
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @Operation(summary = "Update task by Id")
    @PutMapping("/update/{id}")
    public Task updateTask(
            @Parameter(description = "Id of the task to update", example = "7")
            @PathVariable("id") Long id,
            @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @Operation(summary = "Delete task from database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Id of the task to delete", example = "9")
            @PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
