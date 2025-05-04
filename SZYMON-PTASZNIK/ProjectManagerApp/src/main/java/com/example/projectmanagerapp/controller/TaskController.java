package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.services.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@Tag(name="Task", description="opeartions for managing tasks")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    @Operation(summary="List all tasks", description="Returns list of all tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping("/tasks")
    @Operation(summary="Add new task", description="Cretes new task")
    public Task saveTask(@RequestBody Task task) {return taskService.createTask(task);
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "Update an existing user", description = "Updates the details of an existing user by ID")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/tasks/{id}")
    @Operation(summary = "Delete a user", description = "Deletes the user by ID")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
