package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operations related to tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieve a task by its ID")
    public Tasks getTaskById(
            @Parameter(description = "ID of the task to be retrieved", required = true)
            @PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing task", description = "Updates an existing task by its ID")
    public Tasks updateTask(
            @Parameter(description = "ID of the task to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated task data", required = true)
            @RequestBody Tasks updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task by ID", description = "Deletes the task with the specified ID")
    public void deleteTask(
            @Parameter(description = "ID of the task to be deleted", required = true)
            @PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task in the database")
    public Tasks createTask(
            @Parameter(description = "Task to be created", required = true)
            @RequestBody Tasks task) {
        return taskService.createTask(task);
    }
}