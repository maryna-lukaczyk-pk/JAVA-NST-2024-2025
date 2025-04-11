package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @GetMapping
    public Optional<Task> getByID(@PathVariable Long id) {
        return taskRepository.findById(id);
    }

    @PostMapping
    public Task create(@RequestBody Task newTask) {
        return taskRepository.save(newTask);
    }
}