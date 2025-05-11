package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService; // Import serwisu
import com.example.projectmanagerapp.exception.ResourceNotFoundException;

// ... (inne importy Swaggera)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "APIs for managing tasks and their priorities")
public class TaskController {

    private final TaskService taskService; // Wstrzykiwanie serwisu

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{id}/priority/{level}")
    @Operation(summary = "Set task priority") // Adnotacje Swaggera bez zmian
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400"),
            @ApiResponse(responseCode = "409")
    })
    public ResponseEntity<?> setTaskPriority( // Zmieniono na ResponseEntity<?> dla obsługi błędów
                                              @Parameter(description = "ID of the task to update", required = true, example = "1")
                                              @PathVariable Long id,
                                              @Parameter(description = "Priority level to set. Allowed values: HIGH, MEDIUM, LOW", required = true, example = "HIGH")
                                              @PathVariable String level) {
        try {
            Task updatedTask = taskService.setTaskPriority(id, level);
            return ResponseEntity.ok(updatedTask);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) { // Jeśli TaskService rzuca taki wyjątek
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}