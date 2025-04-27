package com.example.demo.controller;

import com.example.demo.entity.Tasks;
import com.example.demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    public Tasks createTask(@RequestBody Tasks task) {
        return taskRepository.save(task);
    }
}