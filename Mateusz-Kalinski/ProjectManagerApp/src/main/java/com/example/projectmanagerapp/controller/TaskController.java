package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name="test", description="test")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Operation(summary = "test", description = "test")
    @GetMapping
    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    @Operation (summary = "test", description = "test")
    @PostMapping
    public Tasks createTask(@RequestBody Tasks task) {
        return taskRepository.save(task);
    }

    @Operation (summary = "test", description = "test")
    @GetMapping("/{id}")
    public Tasks getTaskById(@PathVariable Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Operation (summary = "test", description = "test")
    @PutMapping("/update{id}")
    public Tasks updateTask(@PathVariable Long id, @RequestBody Tasks task) {
        return taskRepository.findById(id).orElse(null);
    }
}