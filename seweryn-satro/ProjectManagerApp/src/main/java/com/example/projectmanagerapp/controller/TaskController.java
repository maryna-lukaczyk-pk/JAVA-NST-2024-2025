package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@Tag(name = "Tasks", description = "Managing tasks")
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    @Operation(summary = "Project task", description = "Returns the list of every task")
    public List<Task> allTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Add new task", description = "Add new task to the database")
    public Task newTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }
}
