package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;
@RestController
@RequestMapping("/api/tasks")

@Tag(
        name = "Task Management",
        description = "APIs for managing tasks"
)

public class TaskController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(TaskController.class);


private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping
    @Operation(
            summary = "Get all tasks",
            description = "Retrieve a list of all tasks"
    )

    public List<Task> getTasks() {
        return taskService.getAllTasks();
    }

    @Operation(
            summary = "Create a new task",
            description = "Create a new task with the provided details"
    )

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }


}