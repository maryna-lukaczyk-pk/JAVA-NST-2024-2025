package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    @Operation(summary = "Dodaj nowe zadanie", description = "Tworzy nowe zadanie")
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj zadanie",
            description = "Aktualizuje istniejące zadanie po ID",
            parameters = {
                    @Parameter(name = "id", description = "ID zadania do aktualizacji", required = true)
            })
    public Task updateTask(@PathVariable("id") Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń zadanie",
               description = "Usuwa zadanie na podstawie ID",
               parameters = {
               @Parameter(name = "id", description = "ID zadania do usunięcia")
               })
    public void deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
    }
}
