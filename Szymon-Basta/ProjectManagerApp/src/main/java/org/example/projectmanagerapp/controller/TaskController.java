package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operations for managing tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // GET
    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks from the database")
    @GetMapping
    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    // POST
    @Operation(summary = "Create a new task", description = "Creates a task with the provided information")
    @PostMapping
    public Tasks createTask(
            @Parameter(description = "Task object to be created")
            @RequestBody Tasks task) {
        return taskRepository.save(task);
    }
}
