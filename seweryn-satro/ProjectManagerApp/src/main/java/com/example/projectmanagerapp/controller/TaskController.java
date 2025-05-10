package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@Tag(name = "Tasks", description = "Managing tasks")
@RequestMapping("api/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    @Operation(summary = "Project task", description = "Returns the list of every task")
    public List<Task> allTasks() {
        return taskService.allTasks();
    }

    @PostMapping("/create")
    @Operation(summary = "Add new task", description = "Add new task to the database")
    public Task newTask(@RequestBody Task task) {
        return taskService.newTask(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Update task data based on task ID")
    public Task updateTask(
            @Parameter(description = "Task ID")
            @PathVariable Integer id,
            @RequestBody Task newTask) {

        return taskService.update(id, newTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Delete task data based on task ID")
    public void deleteTask(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Integer id) {

        taskService.delete(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Return task by ID")
    public Task taskById(@PathVariable Integer id) {
        return taskService.taskById(id);
    }

}
