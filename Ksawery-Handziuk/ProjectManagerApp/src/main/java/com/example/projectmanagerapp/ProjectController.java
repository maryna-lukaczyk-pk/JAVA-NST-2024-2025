// src/main/java/com/example/projectmanagerapp/ProjectController.java
package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.service.ProjectService;
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
import java.util.Set;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Zarządzanie Projektami", description = "API do zarządzania projektami i ich członkami")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Schema(name = "ProjectPayload", description = "Dane wejściowe do tworzenia lub aktualizacji projektu.")
    private static class ProjectPayload {
        @Schema(description = "Nazwa projektu. Musi być unikalna.", example = "Super Projekt", requiredMode = Schema.RequiredMode.REQUIRED)
        private String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @Schema(name = "UserIdPayload", description = "Dane wejściowe zawierające ID użytkownika.")
    private static class UserIdPayload {
        @Schema(description = "ID użytkownika.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        private Long userId;
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }


    // --- POST /api/projects (Tworzenie projektu) ---
    @PostMapping
    @Operation(summary = "Tworzenie nowego projektu", description = "Rejestruje nowy projekt w systemie.")
    @RequestBody(
            description = "Obiekt projektu z nazwą projektu do utworzenia.",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectPayload.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projekt utworzony pomyślnie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe (np. pusta nazwa projektu)", content = @Content()),
            @ApiResponse(responseCode = "409", description = "Nazwa projektu już istnieje", content = @Content())
    })
    public ResponseEntity<?> createProject(@org.springframework.web.bind.annotation.RequestBody ProjectPayload payload) {
        try {
            if (payload.getName() == null || payload.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nazwa projektu nie może być pusta.");
            }
            Project createdProject = projectService.createProject(payload.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // --- GET /api/projects (Pobieranie wszystkich projektów) ---
    @GetMapping
    @Operation(summary = "Pobieranie wszystkich projektów", description = "Pobiera listę wszystkich zarejestrowanych projektów.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano listę projektów", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Project.class)))),
            @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera", content = @Content())
    })
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // --- GET /api/projects/{id} (Pobieranie projektu po ID) ---
    @GetMapping("/{id}")
    @Operation(summary = "Pobieranie projektu po ID", description = "Pobiera określony projekt na podstawie jego ID.")
    @Parameter(name = "id", description = "ID projektu do pobrania", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano projekt", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono projektu", content = @Content())
    })
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        try {
            Project project = projectService.getProjectById(id);
            return ResponseEntity.ok(project);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // --- PUT /api/projects/{id} (Aktualizacja projektu) ---
    @PutMapping("/{id}")
    @Operation(summary = "Aktualizacja istniejącego projektu", description = "Aktualizuje nazwę istniejącego projektu identyfikowanego przez jego ID.")
    @Parameter(name = "id", description = "ID projektu do zaktualizowania", required = true, example = "1")
    @RequestBody(
            description = "Obiekt projektu z nową nazwą.",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectPayload.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projekt zaktualizowany pomyślnie", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe (np. pusta nowa nazwa projektu)", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono projektu do aktualizacji", content = @Content()),
            @ApiResponse(responseCode = "409", description = "Nowa nazwa projektu jest już zajęta", content = @Content())
    })
    public ResponseEntity<?> updateProject(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody ProjectPayload payload) {
        try {
            if (payload.getName() == null || payload.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nowa nazwa projektu nie może być pusta.");
            }
            Project updatedProject = projectService.updateProject(id, payload.getName());
            return ResponseEntity.ok(updatedProject);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicateResourceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // --- DELETE /api/projects/{id} (Usuwanie projektu) ---
    @DeleteMapping("/{id}")
    @Operation(summary = "Usuwanie projektu", description = "Usuwa projekt z systemu na podstawie jego ID.")
    @Parameter(name = "id", description = "ID projektu do usunięcia", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Projekt usunięty pomyślnie (Brak zawartości)"),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono projektu do usunięcia", content = @Content())
    })
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // --- POST /api/projects/{projectId}/users (Dodawanie użytkownika do projektu) ---
    @PostMapping("/{projectId}/users")
    @Operation(summary = "Dodaj użytkownika do projektu", description = "Przypisywanie istniejącego użytkownika do istniejącego projektu.")
    @Parameter(name = "projectId", description = "ID projektu", required = true, example = "1")
    @RequestBody(
            description = "ID użytkownika do dodania.",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserIdPayload.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Użytkownik pomyślnie dodany do projektu", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono projektu lub użytkownika", content = @Content()),
            @ApiResponse(responseCode = "409", description = "Użytkownik jest już przypisany do tego projektu", content = @Content())
    })
    public ResponseEntity<?> addUserToProject(@PathVariable Long projectId, @org.springframework.web.bind.annotation.RequestBody UserIdPayload payload) {
        try {
            Project project = projectService.addUserToProject(projectId, payload.getUserId());
            return ResponseEntity.ok(project);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DuplicateResourceException e) { // Lub inny odpowiedni wyjątek, jeśli użytkownik już jest w projekcie
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalStateException e) { // Np. gdy użytkownik jest już w projekcie
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // --- DELETE /api/projects/{projectId}/users/{userId} (Usuwanie użytkownika z projektu) ---
    @DeleteMapping("/{projectId}/users/{userId}")
    @Operation(summary = "Usuń użytkownika z projektu", description = "Usuwa użytkownika z danego projektu.")
    @Parameter(name = "projectId", description = "ID projektu", required = true, example = "1")
    @Parameter(name = "userId", description = "ID użytkownika do usunięcia", required = true, example = "101")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Użytkownik pomyślnie usunięty z projektu", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono projektu, użytkownika lub przypisania", content = @Content())
    })
    public ResponseEntity<?> removeUserFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        try {
            Project project = projectService.removeUserFromProject(projectId, userId);
            return ResponseEntity.ok(project);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // --- GET /api/projects/{projectId}/users (Pobieranie użytkowników projektu) ---
    @GetMapping("/{projectId}/users")
    @Operation(summary = "Pobierz użytkowników projektu", description = "Pobiera listę użytkowników przypisanych do danego projektu.")
    @Parameter(name = "projectId", description = "ID projektu", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pomyślnie pobrano listę użytkowników projektu", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "404", description = "Nie znaleziono projektu", content = @Content())
    })
    public ResponseEntity<?> getUsersFromProject(@PathVariable Long projectId) {
        try {
            Set<User> users = projectService.getUsersFromProject(projectId);
            return ResponseEntity.ok(users);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}