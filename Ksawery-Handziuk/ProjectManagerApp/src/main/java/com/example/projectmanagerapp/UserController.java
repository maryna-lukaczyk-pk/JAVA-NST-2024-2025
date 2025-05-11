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
import org.springframework.web.bind.annotation.*; // Dodano @PutMapping, @DeleteMapping, @PathVariable

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- DTO dla żądań modyfikujących User (Create/Update) ---
    @Schema(name = "UserPayload", description = "Payload for creating or updating a user.")
    private static class UserPayload {
        @Schema(description = "The username. Must be unique.", example = "updateduser", requiredMode = Schema.RequiredMode.REQUIRED)
        private String username;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }

    // --- POST /api/users (Create) ---
    @PostMapping
    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @RequestBody(
            description = "User object with username to create.",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPayload.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., username empty)", content = @Content()),
            @ApiResponse(responseCode = "409", description = "Username already exists", content = @Content())
    })
    public ResponseEntity<?> createUser(@org.springframework.web.bind.annotation.RequestBody UserPayload payload) {
        try {
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
    @Operation(summary = "Retrieve all users", description = "Fetches a list of all registered users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content())
    })
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // --- GET /api/users/{id} (Get by ID) ---
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID.")
    @Parameter(name = "id", description = "ID of the user to retrieve", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content())
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
    @Operation(summary = "Update an existing user", description = "Updates the username of an existing user identified by their ID.")
    @Parameter(name = "id", description = "ID of the user to update", required = true, example = "1")
    @RequestBody(
            description = "User object with the new username.",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPayload.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., new username empty)", content = @Content()),
            @ApiResponse(responseCode = "404", description = "User not found for update", content = @Content()),
            @ApiResponse(responseCode = "409", description = "New username is already taken by another user", content = @Content())
    })
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody UserPayload payload) {
        try {
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
    @Operation(summary = "Delete a user", description = "Deletes a user from the system by their ID.")
    @Parameter(name = "id", description = "ID of the user to delete", required = true, example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully (No Content)"), // 204 jest typowe dla DELETE
            @ApiResponse(responseCode = "404", description = "User not found for deletion", content = @Content())
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) { // ResponseEntity<Void> dla No Content
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // Zwraca status 204 No Content
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Można też zwrócić .body(e.getMessage())
        }
    }
}