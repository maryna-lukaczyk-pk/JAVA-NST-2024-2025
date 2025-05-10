package org.example.projectmanagerapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task", description = "Operations for mapping tasks")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Retrieve all tasks", description = "Return a list of all tasks from database")
    @GetMapping("/all")
    List<Task> getTasks() {
        return taskService.getTasks();
    }

    @Operation(summary = "Create new task", description = "Adds a new task to database")
    @PostMapping("/create")
    public ResponseEntity<Task> addTask(@RequestBody Task newTask) {
        Task createdTask = taskService.createTask(newTask);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Update task by ID", description = "Updating task by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@Parameter(description = "ID of the task to update") @PathVariable Long id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @Operation(summary = "Delete task by ID", description = "Deleting task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@Parameter(description = "ID of the task to delete") @PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }
}