package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task", description = "Controller for creating and downloading tasks.")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Get a task by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task returned successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tasks.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tasks> getTaskById(@PathVariable Long id) {
        return taskService.getTasksById(id);
    }

    @Operation(summary = "Create a new Tasks.")
    @ApiResponse(responseCode = "200", description = "Task created successfully.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Tasks.class)))
    @PostMapping
    public ResponseEntity<Tasks> createTask(@RequestBody Tasks tasks) {
        return ResponseEntity.ok(taskService.createTask(tasks));
    }


}
