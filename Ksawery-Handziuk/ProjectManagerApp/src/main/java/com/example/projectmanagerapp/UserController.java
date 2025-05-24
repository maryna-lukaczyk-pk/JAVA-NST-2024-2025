// src/main/java/com/example/projectmanagerapp/UserController.java
package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.service.UserService;
import com.example.projectmanagerapp.exception.DuplicateResourceException;
import com.example.projectmanagerapp.exception.ResourceNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Zarządzanie Użytkownikami", description = "API do zarządzania użytkownikami") // Przetłumaczono
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- DTO dla żądań modyfikujących User (Create/Update) ---
    @Schema(name = "UserPayload", description = "Dane wejściowe do tworzenia lub aktualizacji użytkownika.") // Przetłumaczono
    private static class UserPayload {
        @Schema(description = "Nazwa użytkownika. Musi być unikalna.", example = "nowyUzytkownik123", requiredMode = Schema.RequiredMode.REQUIRED) // Przetłumaczono
        private String username;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }

    // --- POST /api/users (Create) ---
    @PostMapping
    @Operation(summary = "Tworzenie nowego użytkownika", description = "Rejestruje nowego użytkownika w systemie.") // Przetłumaczono
    @RequestBody(
            description = "Obiekt użytkownika z nazwą użytkownika do utworzenia.", // Przetłumaczono
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPayload.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Użytkownik utworzony pomyślnie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), // Przetłumaczono
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe (np. pusta nazwa użytkownika)", content = @Content()), // Przetłumaczono
            @ApiResponse(responseCode = "409", description = "Nazwa użytkownika już istnieje", content = @Content()) // Przetłumaczono
    })
    public ResponseEntity<?> createUser(@org.springframework.web.bind.annotation.RequestBody UserPayload payload) {
        try {
            if (payload.getUsername() == null || payload.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nazwa użytkownika nie może być pusta.");
            }
            User createdUser = userService.createUser(payload.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // --- GET /api/users (Get All) ---
    @GetMapping
    @Operation(summary = "Pobieranie wszystkich użytkowników", description = "Pobiera listę wszystkich zarejestrowanych użytkowników.") // Przetłumaczono
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano listę użytkowników", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))), // Przetłumaczono
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera", content = @Content()) // Przetłumaczono
    })
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // --- GET /api/users/{id} (Get by ID) ---
    @GetMapping("/{id}")
    @Operation(summary = "Pobieranie użytkownika po ID", description = "Pobiera określonego użytkownika na podstawie jego ID.") // Przetłumaczono
    @Parameter(name = "id", description = "ID użytkownika do pobrania", required = true, example = "1") // Przetłumaczono
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano użytkownika", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), // Przetłumaczono
            @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika", content = @Content()) // Przetłumaczono
    })
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // --- PUT /api/users/{id} (Update) ---
    @PutMapping("/{id}")
    @Operation(summary = "Aktualizacja istniejącego użytkownika", description = "Aktualizuje nazwę użytkownika istniejącego użytkownika identyfikowanego przez jego ID.") // Przetłumaczono
    @Parameter(name = "id", description = "ID użytkownika do zaktualizowania", required = true, example = "1") // Przetłumaczono
    @RequestBody(
            description = "Obiekt użytkownika z nową nazwą użytkownika.", // Przetłumaczono
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPayload.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Użytkownik zaktualizowany pomyślnie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))), // Przetłumaczono
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe (np. pusta nowa nazwa użytkownika)", content = @Content()), // Przetłumaczono
            @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika do aktualizacji", content = @Content()), // Przetłumaczono
            @ApiResponse(responseCode = "409", description = "Nowa nazwa użytkownika jest już zajęta przez innego użytkownika", content = @Content()) // Przetłumaczono
    })
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody UserPayload payload) {
        try {
            if (payload.getUsername() == null || payload.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nowa nazwa użytkownika nie może być pusta.");
            }
            User updatedUser = userService.updateUser(id, payload.getUsername());
            return ResponseEntity.ok(updatedUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // --- DELETE /api/users/{id} (Delete) ---
    @DeleteMapping("/{id}")
    @Operation(summary = "Usuwanie użytkownika", description = "Usuwa użytkownika z systemu na podstawie jego ID.") // Przetłumaczono
    @Parameter(name = "id", description = "ID użytkownika do usunięcia", required = true, example = "1") // Przetłumaczono
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Użytkownik usunięty pomyślnie (Brak zawartości)"), // Przetłumaczono, 204 jest typowe dla DELETE
            @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika do usunięcia", content = @Content()) // Przetłumaczono
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) { // ResponseEntity<Void> dla braku zawartości
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // Zwraca status 204 Brak zawartości
        } catch (ResourceNotFoundException e) {
            // Można również zwrócić .body(e.getMessage()) jeśli chcemy przekazać treść błędu,
            // ale dla 404 często wystarczy sam status.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}