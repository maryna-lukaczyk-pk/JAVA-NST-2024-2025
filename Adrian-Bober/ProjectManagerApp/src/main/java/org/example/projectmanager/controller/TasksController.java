package org.example.projectmanager.controller;

import org.example.projectmanager.entity.Tasks;
import org.example.projectmanager.repository.TasksRepository;
import org.example.projectmanager.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/tasks")
@Tag (
    name = "Tasks",
    description = "TasksController"
)
public class TasksController {
    @Autowired
    private TasksRepository tasksRepository;

    @Operation (
            summary = "Get all tasks",
            description = "Returns a list of all tasks"
    )

    @GetMapping
    public List<Tasks> getAllTasks(
            @Parameter(
                    name = "opis",
                    description = "Optional description filter",
                    required = false
            )
            @RequestParam(required = false) String opis
    ) {
        return tasksRepository.findAll();
    }

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task and saves it to the database"
    )

    @PostMapping
    public Tasks createTask(
            @Parameter(
                    description = "Task object that needs to be created",
                    required = true
            )
            @RequestBody Tasks task
    ) {
        return tasksRepository.save(task);
    }
}