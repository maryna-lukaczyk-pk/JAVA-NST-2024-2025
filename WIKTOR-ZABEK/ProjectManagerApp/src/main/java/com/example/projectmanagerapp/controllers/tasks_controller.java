package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.tasks;
import com.example.projectmanagerapp.repository.tasks_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class tasks_controller {

    @Autowired
    private tasks_repository tasks_repo;

    @GetMapping
    public List<tasks> getAllTasks() {
        return tasks_repo.findAll();
    }

    @PostMapping("/")
    public tasks createTask(@RequestBody tasks task) {
        return tasks_repo.save(task);
    }
}