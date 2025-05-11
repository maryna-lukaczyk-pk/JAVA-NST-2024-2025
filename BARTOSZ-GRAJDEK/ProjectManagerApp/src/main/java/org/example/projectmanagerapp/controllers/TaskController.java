package org.example.projectmanagerapp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Tasks")
class TaskController {

    private final TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(method = "GET", summary = "Get an array of all tasks")
    @GetMapping("/tasks")
    List<Task> all() {
        return taskService.getAllTasks();
    }

    @Operation(method = "GET", summary = "Get a task by its ID")
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "ID of the task to retrieve") @PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(method = "POST", summary = "Add new post")
    @PostMapping("/tasks")
    public ResponseEntity<Task> newTask(@RequestBody Task newTask) {
        Task createdTask = taskService.createTask(newTask);
        return new ResponseEntity<> (createdTask, HttpStatus.CREATED);
    }

    @Operation(method = "PUT", summary = "Update an existing task by its ID")
    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "ID of the task to update") @PathVariable Long id,
            @RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask)
                .map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(method = "DELETE", summary = "Delete an existing task by its ID")
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID of the task to delete") @PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
