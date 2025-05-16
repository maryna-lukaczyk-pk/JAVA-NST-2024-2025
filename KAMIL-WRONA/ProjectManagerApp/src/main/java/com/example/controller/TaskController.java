package com.example.controller;

import com.example.service.TaskService;
import com.example.entity.Task;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Operations related to managing tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    // GET: Pobierz wszystkie zadania
    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // POST: Dodaj nowe zadanie
    @PostMapping
    @Operation(summary = "Create a new task", description = "Add a new task to the system")
    public Task createTask( @Parameter(description = "Task object to be created", required = true) @RequestBody Task task) {
        return taskService.save(task);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieve a task by its ID")
    public Task getTaskById(
            @Parameter(description = "ID of the task to retrieve", required = true)
            @PathVariable Long id){
        return taskService.getTaskById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id" + id));
   }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
    return taskService.updateTask(id, task)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Delete a task by its ID")
    public void deleteTask(
            @Parameter(description = "ID of the task to delete", required = true)
            @PathVariable Long id) {
        taskService.deleteTask(id);
    }
}