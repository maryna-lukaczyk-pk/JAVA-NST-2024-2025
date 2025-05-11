package org.example.projectmanagerapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@Tag(name = "Kontroler odpowiedzialny za zarządzanie zadaniami")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint odpowiedzialny za pobieranie zadania po id")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return taskService.findTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(description = "Endpoint odpowiedzialny za pobieranie listy wszystkich zadań")
    public List<Task> getAllTasks() {
        return taskService.findAllTasks();
    }

    @PostMapping
    @Operation(description = "Endpoint odpowiedzialny za tworzenie zadania")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }
}