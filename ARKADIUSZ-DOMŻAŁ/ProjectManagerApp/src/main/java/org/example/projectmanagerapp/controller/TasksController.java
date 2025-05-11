package org.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.example.projectmanagerapp.service.TaskService;


import java.util.List;
@RestController
@RequestMapping("api/tasks")
@Tag(name="Tasks")
public class TasksController {
    private final TaskService taskService;

    public TasksController(TaskService taskService)  {
        this.taskService = taskService;
    }

    @Operation(summary = "Retrieve all Tasks", description = "Returns a list of Tasks")
    @GetMapping("/get")
    public List<Tasks> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Create a new Task", description = "Allows to create a new Task")
    @PostMapping("/create")
    public ResponseEntity<Tasks> createTask(
            @Parameter(description="Task object that needs to be created")
            @RequestBody Tasks task) {

        Tasks createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Update the Task", description = "Updates an existing Task by Id")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTask(
            @Parameter(description = "The ID of the Task to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated Task object", required = true)
            @RequestBody Tasks updatedTask) {

        return taskService.updateTask(id, updatedTask)
                .map(updated -> ResponseEntity.ok("Task updated successfully"))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Task with id: " + id + " not found"));
    }

    @Operation(summary = "Delete the Task", description = "Deletes the Task by Id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(
            @Parameter(description = "The ID of the Task to delete", required = true)
            @PathVariable Long id) {

        boolean isDeleted = taskService.deleteTask(id);
        if (isDeleted) {
            return new ResponseEntity<>("Task deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get a Task by ID", description = "Returns a single Task by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<String> getTaskById(
            @Parameter(description = "The ID of the Task to retrieve", required = true)
            @PathVariable Long id) {

        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok("Task found: " + task.getTitle()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Task with id: " + id + " not found"));
    }
}
