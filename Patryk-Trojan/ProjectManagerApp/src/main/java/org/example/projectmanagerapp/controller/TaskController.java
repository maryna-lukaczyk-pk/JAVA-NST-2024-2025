package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
@Tag(name="Task",description = "Operations for managing task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {

        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Return all Tasks",description = "Return a list of all tasks from DB")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Return a Task",description = "Return a task by id from DB")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.findTaskById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new Task",description = "Adds new Task to DB from DB")
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Task", description = "Task a user by id from DB")
    public void updateTask( @PathVariable Long id,@RequestBody Task task) {
        taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Task",description = "Deleting a Task by id from DB")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}