package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task", description = "Task controller")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all tasks", description = "Gets all tasks")
    List<Task> all() {
        return taskService.getAllTasks();
    }

    @PostMapping("/")
    @Operation(summary = "Create task", description = "Creates a new task")
    Task newTask(@RequestBody Task newTask)
    {
        return taskService.createTask(newTask);
    }
}
