package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.schemas.UserDTO;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.projectmanagerapp.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations for managing users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a new user",
    description = "Create a new user in database")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        userService.addUser(userDTO);
        return new ResponseEntity<>("User created", HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all users",
    description = "Get a list of all users from the database")
    public List<User> getUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single user",
            description = "Get a single user with given ID",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)))})
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User ID")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.findUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user",
    description = "Delete a user by ID from database")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User ID")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.deleteUser(id);
            response.put("success", "User deleted");
            return ResponseEntity.ok(response);
        } catch(NotFoundException e) {
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user",
    description = "Update user attributes by ID")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User ID")
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody UserDTO userDTO, @PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.updateUser(userDTO, id);
            response.put("success", "User updated");
            return ResponseEntity.ok(response);
        } catch(NotFoundException e) {
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
