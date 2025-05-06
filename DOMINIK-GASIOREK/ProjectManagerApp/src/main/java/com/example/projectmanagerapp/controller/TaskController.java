package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Task Controller", description="Tasks management")
public class TaskController {

    private TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Operation(summary="Get all tasks", description="Receives all tasks stored")
    @GetMapping("/get-all-tasks")
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Operation(summary="Get task by id", description="Receives task with provided id")
    @GetMapping("/get-task/{id}")
    public Optional<Task> getByID(
            @Parameter(description="Task id") @PathVariable Long id
    ) {
        return taskRepository.findById(id);
    }

    @Operation(summary="Creates new task", description="Creates new task")
    @PostMapping("/create-task")
    public Task create(@RequestBody Task newTask) {
        return taskRepository.save(newTask);
    }
}
