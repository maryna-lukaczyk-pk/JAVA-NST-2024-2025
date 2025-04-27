// Language: java
package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all users", description = "Get a list of all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Create new user", description = "Add a new user to the system")
    public User createUser(
            @Parameter(description = "User data to create a new user", required = true)
            @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Update the user with the given ID")
    public User updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Updated user data", required = true)
            @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing user", description = "Delete the user with the given ID")
    public void deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Integer id) {
        userService.deleteUser(id);
    }
}