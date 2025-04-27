package com.example.demo.controller;

import com.example.demo.entity.Tasks;
import com.example.demo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task Controller")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Returns all tasks")
    public List<Tasks> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @Operation(summary = "Saves current task", description = "Saves current task")
    public Tasks createTask(
            @Parameter(description = "Task object that needs to be saved", required = true)
            @RequestBody Tasks task) {
        return taskService.createTask(task);
    }
}