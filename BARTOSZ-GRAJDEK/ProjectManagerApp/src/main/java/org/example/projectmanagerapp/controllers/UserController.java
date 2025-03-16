package org.example.projectmanagerapp.controllers;

import java.util.List;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {

    private final TaskRepository repository;

    UserController(TaskRepository repository) {
        this.repository= repository;
    }

    @GetMapping("/projects")
    List<Task> all() {
        return repository.findAll();
    }

    @PostMapping("/projects")
    Task newTask(@RequestBody Task newTask) {
        return repository.save(newTask);
    }
}