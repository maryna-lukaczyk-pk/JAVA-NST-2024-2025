package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(
    name = "Task",
    description = "Operations for managing tasks"
)
public class TaskController
{
    @Autowired
    private TaskService taskService;

    @GetMapping
    @Operation(
        method = "GET",
        summary = "Retrieve all tasks",
        description = "Returns a list of all tasks from the database"
    )
    public ResponseEntity<List<Task>> getTasks()
    {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(
        method = "GET",
        summary = "Retrieve task by id",
        description = "Returns a task from the database with a matching id or null"
    )
    @Parameter(
            name = "id",
            description = "Task id",
            required = true,
            example = "1"
    )
    public ResponseEntity<Task> getTaskById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    @Operation(
        method = "POST",
        summary = "Create a new task",
        description = "Creates a new task in the database from request data and returns it"
    )
    public ResponseEntity<Task> createTask(
        @RequestBody Task task
    ) {
        task.setId(null);
        Task newTask = taskService.createTask(task);

        return ResponseEntity.ok(newTask);
    }

    @PutMapping
    @Operation(
            method = "PUT",
            summary = "Update a task",
            description = "Updates an existing task with values from request body and returns it"
    )
    public ResponseEntity<Task> updateTask(
            @RequestBody Task task
    ) {
        Task updatedTask = taskService.updateTask(task);

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping
    @Operation(
            method = "DELETE",
            summary = "Delete a task",
            description = "Deletes a task from the database with a matching id and returns it"
    )
    public ResponseEntity<Task> deleteTask(
            Long id
    ) {
        Task deletedTask = taskService.deleteTask(id);

        return ResponseEntity.ok(deletedTask);
    }
}
