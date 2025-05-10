package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tasks", description = "Operacje na zadaniach")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Pobierz wszystkie zadania")
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }


    @Operation(summary = "Dodaj nowe zadanie")
    @PostMapping
    public Task createTask(
            @Parameter(description = "Dane nowego zadania") @RequestBody Task task
    ) {
        return taskService.createTask(task);
    }

    @Operation(summary = "Zaktualizuj zadanie")
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @Operation(summary = "Usuń zadanie")
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

}
