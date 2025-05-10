package org.example.projectmanagerapp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.services.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@Tag(name = "Tasks", description = "Operations for managing tasks")
class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks from the database")
    public List<Tasks> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("tasks/{id}")
    @Operation(summary = "Get task by ID", description = "Returns task details by ID")
    public Tasks getTaskById(@PathVariable Long id) {return taskService.getTaskById(id);}


    @PostMapping("/tasks")
    @Operation(summary = "Create a new task", description = "Adds a new task to the database")
    public Tasks newTask(@RequestBody Tasks newTask) {
        return taskService.createTask(newTask);
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "Update an existing task", description = "Updates the details of an existing task by ID")
    public Tasks updateTask(@PathVariable Long id, @RequestBody Tasks task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/tasks/{id}")
    @Operation(summary = "Delete a task", description = "Deletes the task by ID")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
