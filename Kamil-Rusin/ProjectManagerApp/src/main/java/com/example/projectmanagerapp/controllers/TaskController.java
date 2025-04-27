package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    public final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/all")
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable("id") Long id) {
        return taskRepository.findById(id);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        taskRepository.save(task);
        return task;
    }
}
