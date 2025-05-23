package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operacje związane z zadaniami")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Pobierz listę wszystkich zadań", description = "Zwraca listę wszystkich zadań")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz zadanie po ID")
    public Task getTaskById(
            @Parameter(description = "ID zadania") @PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    @Operation(summary = "Dodaj nowe zadanie", description = "Tworzy nowe zadanie")
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Zaktualizuj zadanie po ID")
    public Task updateTask(
            @Parameter(description = "ID zadania do aktualizacji") @PathVariable Long id,
            @RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń zadanie po ID")
    public void deleteTask(
            @Parameter(description = "ID zadania do usunięcia") @PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
