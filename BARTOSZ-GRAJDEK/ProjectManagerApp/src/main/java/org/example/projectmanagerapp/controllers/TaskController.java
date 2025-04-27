package org.example.projectmanagerapp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Tasks")
class TaskController {

    private final TaskRepository repository;

    TaskController(TaskRepository repository) {
        this.repository= repository;
    }

    @Operation(method = "GET", summary = "Get an array of all tasks")
    @GetMapping("/tasks")
    List<Task> all() {
        return repository.findAll();
    }

    @Operation(method = "POST", summary = "Add new post")
    @PostMapping("/tasks")
    Task newTask(@RequestBody Task newTask) {
        return repository.save(newTask);
    }
}