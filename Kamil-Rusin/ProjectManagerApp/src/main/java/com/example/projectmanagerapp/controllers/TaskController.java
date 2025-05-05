package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Tag(name = "Tasks", description = "Operations to manage tasks")
@RestController
@RequestMapping("api/tasks")
public class TaskController {
    public final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Operation(summary = "Retrieve all tasks")
    @GetMapping("/all")
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Operation(summary = "Retrieve task by Id")
    @GetMapping("/{id}")
    public Optional<Task> getTaskById(
            @Parameter(description = "Id of the task to retrieve", example = "5")
            @PathVariable("id") Long id) {
        return taskRepository.findById(id);
    }

    @Operation(summary = "Save task to database")
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        taskRepository.save(task);
        return task;
    }
}
