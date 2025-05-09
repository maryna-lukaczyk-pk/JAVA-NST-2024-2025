package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Task API", description = "Task management operations")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    @Operation(summary = "Create a new task", description = "Create a new task in the system")
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }
}
