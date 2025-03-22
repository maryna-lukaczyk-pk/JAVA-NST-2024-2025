package org.example.projectmanagerapp.controllers;

import java.util.List;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TaskController {

    private final TaskRepository repository;

    TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/tasks")
    List<Tasks> all() {
        return repository.findAll();
    }

    @PostMapping("/tasks")
    Tasks newTask(@RequestBody Tasks newTask) {
        return repository.save(newTask);
    }
}