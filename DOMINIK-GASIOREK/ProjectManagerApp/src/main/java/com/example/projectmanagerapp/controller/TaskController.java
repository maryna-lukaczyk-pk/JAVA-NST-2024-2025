package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Task Controller", description="Tasks management")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary="Get all tasks", description="Receives all tasks stored")
    @GetMapping("/get-all-tasks")
    public List<Task> getAll() {
        return taskService.getAll();
    }

    @Operation(summary="Get task by id", description="Receives task with provided id")
    @GetMapping("/get-task/{id}")
    public Optional<Task> getByID(
            @Parameter(description="Task id") @PathVariable Long id
    ) {
        return taskService.getByID(id);
    }

    @Operation(summary="Creates new task", description="Creates new task")
    @PostMapping("/create-task")
    public Task create(@RequestBody Task newTask) {
        return taskService.create(newTask);
    }

    @Operation(summary="Updates existing task", description="Updates a task with the given id")
    @PutMapping("/update-task/{id}")
    public Optional<Task> update(
            @Parameter(description = "Task id") @PathVariable Long id,
            @RequestBody Task updatedTask
    ) {
        return taskService.update(id, updatedTask);
    }

    @Operation(summary="Deletes task by id", description="Deletes the task with the given id")
    @DeleteMapping("/delete-task/{id}")
    public void delete(
            @Parameter(description = "Task id") @PathVariable Long id
    ) {
        taskService.delete(id);
    }
}
