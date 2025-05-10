package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Controller dla taskow")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Zwraca wszystkie taski", description = "Zwraca array tasków z bazy danych")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz task po ID", description = "Zwraca pojedynczy task na podstawie jego ID")
    public ResponseEntity<Task> getTaskById(
            @Parameter(description = "ID taska do pobrania") @PathVariable Integer id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Dodanie nowego tasku", description = "Dodaje nowy task do bazy danych")
    public ResponseEntity<Task> createTask(
            @Parameter(description = "JSON taska do dodania") @RequestBody Task task) {
        // Baza danych przypisze ID
        task.setId(null);
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj task", description = "Aktualizuje istniejący task na podstawie ID")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "ID taska do aktualizacji") @PathVariable Integer id,
            @Parameter(description = "Zaktualizowane dane taska") @RequestBody Task taskDetails) {
        Task updatedTask = taskService.updateTask(id, taskDetails);
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń task", description = "Usuwa task na podstawie podanego ID")
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID taska do usunięcia") @PathVariable Integer id) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            taskService.deleteTaskById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}