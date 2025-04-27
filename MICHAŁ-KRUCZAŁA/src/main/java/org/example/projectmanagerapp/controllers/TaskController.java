package org.example.projectmanagerapp.controllers;


import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;


    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }
}