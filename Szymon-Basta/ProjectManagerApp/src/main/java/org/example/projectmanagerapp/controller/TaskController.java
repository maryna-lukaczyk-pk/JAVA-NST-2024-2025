package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operations for managing tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks from the database")
    @GetMapping
    public List<Tasks> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Create a new task", description = "Creates a task with the provided information")
    @PostMapping
    public Tasks createTask(
            @Parameter(description = "Task object to be created")
            @RequestBody Tasks task) {
        return taskService.createTask(task);

    }

    @Operation(summary = "Update an existing task", description = "Updates a task by its ID")
    @PutMapping("/{id}")
    public Tasks updateTask(
            @Parameter(description = "ID of the task to be updated") @PathVariable Long id,
            @RequestBody Tasks updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    @Operation(summary = "Delete a task", description = "Deletes a task by its ID")
    @DeleteMapping("/{id}")
    public void deleteTask(
            @Parameter(description = "ID of the task to be deleted") @PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<Tasks> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }
}
