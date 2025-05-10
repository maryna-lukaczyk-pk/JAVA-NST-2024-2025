package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task", description = "Task management")
public class TaskController {

    // taskRepository field declaration
    // "final" means that a field needs to be initialized
    // using constructor and cannot be changed later
    private final TaskService taskService;

    // constructor for TaskController class
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Returns list of all tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task")
    public Task createTask(
            @Parameter(description = "Task to be created" ,required = true)
            @RequestBody Task task
    ) {
        return taskService.createTask(task);
    }
}