package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.dto.TaskDTO;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Task operations")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) { this.taskService = taskService; }

    @GetMapping
    @Operation(summary = "Download all tasks", description = "Returns a list of all tasks")
    public List<TaskDTO> getAllTasks() { return taskService.getAllTasks(); }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public TaskDTO getTaskById(
            @Parameter(description = "Task ID", example = "1")
            @PathVariable Long id) { return taskService.getTaskById(id); }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Adds a new task to the system")
    public TaskDTO createTask(@Valid @RequestBody Task task) { return taskService.createTask(task); }

    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Updates the task with the given ID")
    public TaskDTO updateTask(
            @Parameter(description = "Task ID to update", example = "1")
            @PathVariable Long id,
            @RequestBody Task task) { return taskService.updateTask(id, task); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Deletes the task with the given ID")
    public void deleteTask(
            @Parameter(description = "ID of the task to be deleted", example = "1")
            @PathVariable Long id) { taskService.deleteTask(id); }
}
