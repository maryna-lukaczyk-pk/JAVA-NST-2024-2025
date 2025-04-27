package org.jerzy.projectmanagerapp.controller;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.Task;
import org.jerzy.projectmanagerapp.repository.TaskRepository;
import org.jerzy.projectmanagerapp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Task", description = "Task managment methods")
@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskRepository repository) {
        this.service = new TaskService(repository);
    }

    @Operation(summary = "List all tasks")
    @GetMapping
    public List<Task> get() {
        return service.getAllTasks();
    }

    @Operation(summary = "Create new task")
    @Parameter
    @PostMapping
    public ResponseEntity<Task> post(@RequestBody() Task task) {
        return new ResponseEntity<>(this.service.create(task), HttpStatus.CREATED);
    }
}
