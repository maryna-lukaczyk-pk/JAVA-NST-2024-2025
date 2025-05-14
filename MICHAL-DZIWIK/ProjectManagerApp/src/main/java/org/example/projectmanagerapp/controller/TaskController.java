package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operations related to project tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks.")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Adds a new task to the database.")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task created = taskService.createTask(task);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Returns a task by its ID.")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task t = taskService.getTaskById(id);
        return t != null
                ? ResponseEntity.ok(t)
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Updates an existing task by its ID.")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @RequestBody Task incoming
    ) {
        Task existing = taskService.getTaskById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        existing.setTitle(incoming.getTitle());
        existing.setDescription(incoming.getDescription());
        if (incoming.getTaskType() != null) {
            existing.setTaskType(incoming.getTaskType());
        }
        if (incoming.getPriority() != null) {
            existing.setPriority(incoming.getPriority());
        }
        Task updated = taskService.updateTask(id, existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/clear-all")
    @Operation(summary = "Clear all tasks", description = "Removes all tasks from the database (for testing).")
    public ResponseEntity<Void> clearAllTasks() {
        taskService.deleteAllTasks();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Deletes a task by its ID.")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}
