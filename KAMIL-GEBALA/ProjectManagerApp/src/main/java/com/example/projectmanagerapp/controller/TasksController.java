package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.repository.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

    @Autowired
    private TasksRepository tasksRepository;

    @GetMapping
    public List<Tasks> getAllTasks() {
        return tasksRepository.findAll();
    }

    @PostMapping
    public Tasks createTask(@RequestBody Tasks task) {
        return tasksRepository.save(task);
    }
}
