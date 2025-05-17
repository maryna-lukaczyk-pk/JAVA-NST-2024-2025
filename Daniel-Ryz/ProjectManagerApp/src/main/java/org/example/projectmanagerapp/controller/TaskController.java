package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.CreateTaskRequest;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task", description = "Controller for creating and downloading tasks.")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Get a list of all tasks.")
    @ApiResponse(responseCode = "200", description = "Tasks list returned successfully.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Tasks.class)))
    @GetMapping
    public List<Tasks> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Get a task by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task returned successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tasks.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @GetMapping("/{id}")
    public Tasks getTaskById(
            @Parameter(description = "Task ID", example = "1")
            @PathVariable Long id) {
        return taskService.getTasksById(id);
    }

    @Operation(summary = "Create a new Tasks.")
    @ApiResponse(responseCode = "201", description = "Task created successfully.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Tasks.class)))
    @PostMapping
    public ResponseEntity<Tasks> createTask( @RequestBody CreateTaskRequest request) {
        Tasks task = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @Operation(summary = "Update a task by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tasks.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @PutMapping("/{id}")
    public Tasks updateTask(
            @Parameter(description = "Task ID", example = "1")
            @PathVariable Long id, @RequestBody CreateTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @Operation(summary = "Delete a task by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task deleted successfully.",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @Parameter(description = "Task ID", example = "1")
            @PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully.");
    }

    @Operation(summary = "Assign Task to Project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task assigned to project successfully.",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Task or Project not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @PostMapping("/{taskId}/projects/{projectId}")
    public ResponseEntity<String> assignTaskToProject(
            @Parameter(description = "Task ID", example = "1")
            @PathVariable Long taskId,
            @Parameter(description = "Project ID", example = "1")
            @PathVariable Long projectId) {
        taskService.assignTaskToProject(taskId, projectId);
        return ResponseEntity.ok("Project assigned to user successfully.");
    }


}
