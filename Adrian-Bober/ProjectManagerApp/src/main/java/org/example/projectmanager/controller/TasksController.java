package org.example.projectmanager.controller;

import org.example.projectmanager.entity.Tasks;
//import org.example.projectmanager.repository.TasksRepository;
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
@Tag (name = "Tasks",description = "TasksController")

public class TasksController {
    @Autowired
    //private TasksRepository tasksRepository;
    private TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping
    @Operation (summary = "Get all tasks",description = "Returns a list of all tasks")
    public List<Tasks> getAllTasks(
            @Parameter(name = "opis",description = "Optional description filter",required = false)
            @RequestParam(required = false) String opis
    ) {
        return tasksService.getAllTasks();
    }

    @PostMapping
    @Operation(summary = "Create a new task",description = "Creates a new task and saves it to the database")
    public Tasks createTask(
            @Parameter(description = "Task object that needs to be created",required = true)
            @RequestBody Tasks task
    ) {
        return tasksService.createTask(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Updates an existing task by ID")
    public ResponseEntity<Tasks> updateTasks(
            @Parameter(description = "ID of the task to be updated", required = true)
            @PathVariable Long id,
            @RequestBody Tasks tasks) {
        Tasks updatedTasks = tasksService.updateTasks(id, tasks);
        return ResponseEntity.ok(updatedTasks);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Deletes a task by ID")
    public ResponseEntity<Void> deleteTasks(
            @Parameter(description = "ID of the task to be deleted", required = true)
            @PathVariable Long id) {
        tasksService.deleteTasks(id);
        return ResponseEntity.noContent().build();
    }
}