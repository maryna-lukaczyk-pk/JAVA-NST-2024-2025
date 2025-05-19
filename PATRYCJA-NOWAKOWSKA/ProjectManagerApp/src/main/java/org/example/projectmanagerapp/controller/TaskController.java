package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.projectmanagerapp.service.TaskService;


import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Zarządzanie zadaniami w projektach")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkie zadania", description = "Zwraca listę zadań z bazy danych")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz zadanie po ID", description = "Zwraca jedno zadanie na podstawie ID")
    public Task getTaskById(
            @Parameter(description = "ID zadania do pobrania", required = true)
            @PathVariable Long id) {
        return taskService.getTaskById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @PostMapping
    @Operation(summary = "Utwórz nowe zadanie", description = "Dodaje zadanie do bazy i zwraca je z przydzielonym ID")
    public Task createTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Reprezentacja zadania do utworzenia")
            @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj zadanie", description = "Aktualizuje dane zadania na podstawie ID")
    public Task updateTask(
            @Parameter(description = "ID zadania, które ma zostać zaktualizowane", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nowe dane zadania do aktualizacji", required = true)
            @RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń zadanie", description = "Usuwa zadanie na podstawie ID")
    public void deleteTask(
            @Parameter(description = "ID zadania, które ma zostać usunięte", required = true)
            @PathVariable Long id) {
        taskService.deleteTask(id);
    }
}

