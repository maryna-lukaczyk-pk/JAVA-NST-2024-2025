package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task API", description = "Task management operations")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks")
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Create a new task")
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @Operation(summary = "Update task by ID")
    @PutMapping("/{id}")
    public Task updateTask(
        @Parameter(description = "ID of the task to update") @PathVariable Long id,
        @RequestBody Task task
    ) {
        return taskService.updateTask(id, task);
    }

    @Operation(summary = "Delete task by ID")
    @DeleteMapping("/{id}")
    public void deleteTask(
        @Parameter(description = "ID of the task to delete") @PathVariable Long id
    ) {
        taskService.deleteTask(id);
    }
}
