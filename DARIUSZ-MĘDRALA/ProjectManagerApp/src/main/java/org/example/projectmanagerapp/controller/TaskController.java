package org.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Task API", description = "API for managing tasks")
@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return taskService.findAll();
    }

    @Operation(summary = "Create a new task", description = "Create a new task with the provided details")
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a task", description = "Delete a task by its ID")
    @Parameter(name = "id", description = "ID of the task to delete")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update a task", description = "Update an existing task by its ID")
    @Parameter(name = "id", description = "ID of the task to update")
    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @Operation(summary = "Get a task by ID", description = "Retrieve a task by its ID")
    @Parameter(name = "id", description = "ID of the task to retrieve")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Operation(summary = "Assign a task to a project", description = "Assign a task to a project by their IDs")
    @Parameter(name = "taskId", description = "ID of the task to assign")
    @Parameter(name = "projectId", description = "ID of the project to assign the task to")
    @PostMapping("/assign/{taskId}/{projectId}")
    public ResponseEntity<Void> assignTaskToProject(@PathVariable Long taskId, @PathVariable Long projectId) {
        taskService.assignTaskToProject(taskId, projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
