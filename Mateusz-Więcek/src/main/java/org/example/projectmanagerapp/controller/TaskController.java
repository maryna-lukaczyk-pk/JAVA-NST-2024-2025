package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tasks", description = "Operations related to tasks")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService svc;
    public TaskController(TaskService svc) { this.svc = svc; }

    @Operation(summary = "Get all tasks")
    @GetMapping
    public List<Task> getAllTasks() { return svc.getAllTasks(); }

    @Operation(summary = "Get task by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return svc.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new task")
    @PostMapping
    public Task createTask(@RequestBody Task t) { return svc.createTask(t); }

    @Operation(summary = "Update task by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task d) {
        return svc.updateTask(id, d)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return svc.deleteTask(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
