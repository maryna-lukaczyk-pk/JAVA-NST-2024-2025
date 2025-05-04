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

    @GetMapping
    @Operation(summary = "Get all tasks")
    public List<Tasks> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public Tasks createTask(
            @Parameter(description = "Task to be created", required = true)
            @RequestBody Tasks task) {
        return taskService.createTask(task);
    }

    // ========================
// Metoda PUT (aktualizacja)
// ========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update existing task",
            description = "Updates an existing task by its ID"
    )
    public Tasks updateTask(
            @Parameter(description = "ID of the task to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated task data", required = true)
            @RequestBody Tasks updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    // ==========================
// Metoda DELETE (usuwanie)
// ==========================
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a task by ID",
            description = "Deletes the task with the specified ID"
    )
    public void deleteTask(
            @Parameter(description = "ID of the task to be deleted", required = true)
            @PathVariable Long id) {
        taskService.deleteTask(id);
    }
}