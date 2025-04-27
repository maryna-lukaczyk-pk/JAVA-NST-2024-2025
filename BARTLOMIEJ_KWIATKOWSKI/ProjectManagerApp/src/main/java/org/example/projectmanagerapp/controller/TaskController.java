package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operacje związane z zadaniami")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    @Operation(summary = "Pobierz wszystkie zadania")
    public List<Tasks> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @Operation(summary = "Utwórz nowe zadanie")
    public Tasks createTask(@RequestBody @Parameter(description = "Dane nowego zadania") Tasks tasks) {
        return taskService.createTask(tasks);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj zadanie")
    public Tasks updateTask(@PathVariable Long id, @RequestBody Tasks taskDetails) {
        return taskService.updateTask(id, taskDetails);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń zadanie")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}