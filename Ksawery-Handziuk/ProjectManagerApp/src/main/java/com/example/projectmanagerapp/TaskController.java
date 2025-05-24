// src/main/java/com/example/projectmanagerapp/TaskController.java
package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService; // Import serwisu
import com.example.projectmanagerapp.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
// usunięto nieużywane importy Swaggera: Content, Schema
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
@Tag(name = "Zarządzanie Zadaniami", description = "API do zarządzania zadaniami i ich priorytetami") // Przetłumaczono
public class TaskController {

    private final TaskService taskService; // Wstrzykiwanie serwisu

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{id}/priority/{level}")
    @Operation(summary = "Ustaw priorytet zadania") // Adnotacje Swaggera - przetłumaczono
    @ApiResponses(value = { // Przykładowe kody odpowiedzi
            @ApiResponse(responseCode = "200", description = "Priorytet zadania pomyślnie ustawiony"), // Zmieniono z 201 na 200 dla aktualizacji
            @ApiResponse(responseCode = "400", description = "Nieprawidłowy poziom priorytetu lub ID zadania"),
            @ApiResponse(responseCode = "404", description = "Zadanie nie znalezione")
            // Usunięto 409, bo nie jest jasne, jaki konflikt miałby tu wystąpić
    })
    public ResponseEntity<?> setTaskPriority(
            @Parameter(description = "ID zadania do aktualizacji", required = true, example = "1") // Przetłumaczono
            @PathVariable Long id,
            @Parameter(description = "Poziom priorytetu do ustawienia. Dozwolone wartości: HIGH, MEDIUM, LOW", required = true, example = "HIGH") // Przetłumaczono
            @PathVariable String level) {
        try {
            // Założenie: TaskService tworzy zadanie, jeśli nie istnieje, lub aktualizuje istniejące.
            // Dla uproszczenia, jeśli zadanie nie istnieje, rzuci ResourceNotFoundException.
            Task updatedTask = taskService.setTaskPriority(id, level);
            return ResponseEntity.ok(updatedTask); // Zwraca zaktualizowane zadanie i status 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) { // Jeśli TaskService rzuca taki wyjątek dla niepoprawnego 'level'
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}