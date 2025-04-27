// Language: java
package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Operations related to tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all tasks", description = "Get a list of all tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @Operation(summary = "Create new task", description = "Add a new task to the system")
    public Task createTask(
            @Parameter(description = "Task data to create a new task", required = true)
            @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task", description = "Update the task with the given ID")
    public Task updateTask(
            @Parameter(description = "ID of the task to update", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Updated task data", required = true)
            @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing task", description = "Delete the task with the given ID")
    public void deleteTask(
            @Parameter(description = "ID of the task to delete", required = true)
            @PathVariable Integer id) {
        taskService.deleteTask(id);
    }
}