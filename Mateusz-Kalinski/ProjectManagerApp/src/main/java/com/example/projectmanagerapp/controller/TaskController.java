package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Endpoints for managing tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    @GetMapping
    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    @Operation(summary = "Get task by ID", description = "Retrieve a specific task by its ID")
    @Parameter(name = "id", description = "ID of the task to retrieve", required = true)
    @GetMapping("/{id}")
    public Tasks getTaskById(@PathVariable Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Operation(summary = "Create new task", description = "Create a new task with provided details")
    @PostMapping
    public Tasks createTask(
            @Parameter(name = "task", description = "Task object to create", required = true)
            @RequestBody Tasks task) {
        return taskRepository.save(task);
    }

    @Operation(summary = "Update task", description = "Update an existing task by its ID")
    @Parameter(name = "id", description = "ID of the task to update", required = true)
    @PutMapping("/update/{id}")
    public Tasks updateTask(
            @PathVariable Long id,
            @Parameter(name = "task", description = "Updated task object", required = true)
            @RequestBody Tasks task) {
        task.setId(id);
        return taskRepository.save(task);
    }

    @Operation(summary = "Delete task", description = "Delete a task by its ID")
    @Parameter(name = "id", description = "ID of the task to delete", required = true)
    @DeleteMapping("/delete/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
}