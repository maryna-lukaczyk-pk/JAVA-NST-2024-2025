package org.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "User API", description = "API for managing users")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping("/all")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @Operation(summary = "Create a new user", description = "Create a new user with the provided details")
    @PostMapping("/create")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a user", description = "Delete a user by its ID")
    @Parameter(name = "id", description = "ID of the user to delete")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update a user", description = "Update an existing user by its ID")
    @Parameter(name = "id", description = "ID of the user to update")
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @Operation(summary = "Get a user by ID", description = "Retrieve a user by its ID")
    @Parameter(name = "id", description = "ID of the user to retrieve")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
