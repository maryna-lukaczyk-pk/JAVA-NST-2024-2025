package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task", description = "Task management")
public class TaskController {

    // taskRepository field declaration
    // "final" means that a field needs to be initialized
    // using constructor and cannot be changed later
    private final TaskService taskService;

    // constructor for TaskController class
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all tasks", description = "Returns list of all tasks")
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Returns task by ID")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "ID of task to be retrieved" ,required = true)
            @PathVariable Long id
    ) {
        return taskService.findById(id)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new task", description = "Creates a new task")
    public Task createTask(
            @Parameter(description = "Task to be created" ,required = true)
            @RequestBody Task task
    ) {
        return taskService.create(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task", description = "Updates an existing task")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "ID of task to be updated" ,required = true)
            @PathVariable Long id, @Parameter(description = "New task data" ,required = true)
            @RequestBody Task task
    ) {
        return taskService.findById(id)
                          .map(existing -> ResponseEntity.ok(taskService.update(id, task)))
                          .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing task", description = "Deletes an existing task")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of task to be deleted" ,required = true)
            @PathVariable Long id
    ) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}