package org.example.projectmanagerapp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Tasks", description = "Operations for managing tasks")
class TaskController {

    private final TaskRepository repository;

    TaskController(TaskRepository repository) {
        this.repository = repository;
    }
    @GetMapping("/tasks")
    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks from the database")
    List<Tasks> all() {
        return repository.findAll();
    }
    @PostMapping("/tasks")
    @Operation(summary = "Create a new task", description = "Adds a new task to the database")
    Tasks newTask(@RequestBody Tasks newTask) {
        return repository.save(newTask);
    }
}
