package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.schemas.TaskDTO;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operations for managing tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create a new task",
            description = "Create a new task in database")
    public ResponseEntity<Map<String, String>> createTask(@RequestBody TaskDTO taskDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            taskService.addTask(taskDTO);
            response.put("success", "Task created");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (HttpClientErrorException e) {
            response.put("error", e.getStatusText());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    @Operation(summary = "Get all tasks",
    description = "Get a list of all tasks from the database")
    public List<Task> getTasks() {
        return taskService.findAllTasks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single task",
    description = "Get a single task with given ID",
    responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Task.class)))})
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Task ID")
    public ResponseEntity<?> getTaskById(@PathVariable Integer id) {
        try {
            Task task = taskService.findTaskById(id);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (NotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Task not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task",
    description = "Delete a task by ID from database")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Task ID")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            taskService.deleteTask(id);
            response.put("success", "Task deleted");
            return ResponseEntity.ok(response);
        } catch(NotFoundException e) {
            response.put("error", "Task not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update task attributes",
    description = "Update task attribute/attributes by ID")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Task ID")
    public ResponseEntity<Map<String, String>> updateProject(@RequestBody TaskDTO taskDTO, @PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();

        try {
            taskService.updateTaskAttributes(taskDTO, id);
            response.put("success", "Task attributes updated");
            return ResponseEntity.ok(response);
        } catch(NotFoundException e) {
            response.put("error", "Task not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
