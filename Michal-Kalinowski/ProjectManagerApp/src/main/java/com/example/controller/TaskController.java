package com.example.controller;

import com.example.model.Task;
import com.example.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // GET: Pobierz wszystkie zadania
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // POST: Dodaj nowe zadanie
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }
}