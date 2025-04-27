package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.repozytorium.ProjectRepository;
import com.example.projectmanagerapp.repozytorium.TasksRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "API for managing tasks.")
public class TasksController {
    private final TasksRepository tasksRepository;

    @Autowired
    public TasksController(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    // Get task by id
    @GetMapping("/{id}")
    @Operation(
            summary = "Find task by ID",
            description = "Returns a task matching the provided ID."
    )
    public ResponseEntity<Tasks> getTaskById(
            @Parameter(description = "ID of the task to be retrieved") @PathVariable Long id) {
        Optional<Tasks> task = tasksRepository.findById(id);
        task.ifPresent(Tasks::generatePriority);  // Avoid calling on empty Optional
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new task
    @PostMapping
    @Operation(
            summary = "Create a new task",
            description = "Creates a new task and returns the created entity."
    )
    public ResponseEntity<Tasks> createTask(
            @Parameter(description = "Details of the task to be created") @RequestBody Tasks tasks) {
        Tasks createdTasks = tasksRepository.save(tasks);
        return new ResponseEntity<>(createdTasks, HttpStatus.CREATED);
    }
}
