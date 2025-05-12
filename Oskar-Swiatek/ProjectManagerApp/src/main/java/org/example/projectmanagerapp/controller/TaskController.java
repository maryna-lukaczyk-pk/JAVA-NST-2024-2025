package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.task.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Tasks", description = "Operations related to tasks")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Retrieve all tasks")
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Retrieve a task by ID")
    @GetMapping("/{id}")
    @Parameter(description = "ID of the task to retrieve")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @Operation(summary = "Create a new task")
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @Operation(summary = "Update an existing task")
    @PutMapping("/{id}")
    @Parameter(description = "ID of the task to update")
    public Task updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        return taskService.updateTask(id, taskDetails);
    }

    @Operation(summary = "Delete a task by ID")
    @DeleteMapping("/{id}")
    @Parameter(description = "ID of the task to delete")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}