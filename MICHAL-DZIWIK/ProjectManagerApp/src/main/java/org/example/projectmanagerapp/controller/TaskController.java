package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operations related to project tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks from the database.")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Adds a new task to the database.")
    public Task createTask(
            @Parameter(description = "Task object to be created")
            @RequestBody Task task
    ) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task", description = "Updates an existing task by its ID.")
    public Task updateTask(
            @Parameter(description = "ID of the task to be updated")
            @PathVariable Long id,
            @Parameter(description = "Updated task object")
            @RequestBody Task task
    ) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Deletes a task by its ID.")
    public void deleteTask(
            @Parameter(description = "ID of the task to be deleted")
            @PathVariable Long id
    ) {
        taskService.deleteTask(id);
    }
}
