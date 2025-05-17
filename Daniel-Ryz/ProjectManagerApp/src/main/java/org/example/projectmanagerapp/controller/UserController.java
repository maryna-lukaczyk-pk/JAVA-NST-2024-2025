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
import org.example.projectmanagerapp.dto.CreateUserRequest;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "Controller for creating and downloading users.")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Gets a list of all users.")
    @ApiResponse(responseCode = "200", description = "Users list returned successfully.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Users.class)))
    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get a user by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User returned successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Users.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @GetMapping("/{id}")
    public Users getUserById(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Create a new User.")
    @ApiResponse(responseCode = "201", description = "User created successfully.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Users.class)))
    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody CreateUserRequest request) {
        Users user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Update a user by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Users.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @PutMapping("/{id}")
    public Users updateUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id, @RequestBody CreateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @Operation(summary = "Delete a User by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully.",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @Operation(summary = "Assign Project to User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project assigned to user successfully.",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "User or Project not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @PostMapping("/{userId}/projects/{projectId}")
    public ResponseEntity<String> assignProjectToUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "Project ID", example = "1")
            @PathVariable Long projectId) {
        userService.assignProjectToUser(userId, projectId);
        return ResponseEntity.ok("Project assigned to user successfully.");
    }


}
