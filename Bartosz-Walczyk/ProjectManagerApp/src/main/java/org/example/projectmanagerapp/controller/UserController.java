package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(
    name = "User",
    description = "Operations for managing users"
)
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(
        method = "GET",
        summary = "Retrieve all users",
        description = "Returns a list of all users from the database"
    )
    public ResponseEntity<List<User>> getUsers()
    {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(
        method = "GET",
        summary = "Retrieve user by id",
        description = "Returns a user from the database with a matching id or null"
    )
    @Parameter(
            name = "id",
            description = "User id",
            required = true,
            example = "1"
    )
    public ResponseEntity<User> getUserById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @Operation(
        method = "POST",
        summary = "Create a new user",
        description = "Creates a new user in the database from request data and returns it"
    )
    public ResponseEntity<User> createUser(
        @RequestBody User user
    ) {
        user.setId(null);
        User newUser = userService.createUser(user);

        return ResponseEntity.ok(newUser);
    }

    @PutMapping
    @Operation(
            method = "PUT",
            summary = "Update a user",
            description = "Updates an existing user with values from request body and returns it"
    )
    public ResponseEntity<User> updateUser(
            @RequestBody User user
    ) {
        User updatedUser = userService.updateUser(user);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    @Operation(
            method = "DELETE",
            summary = "Delete a user",
            description = "Deletes a user from the database with a matching id and returns it"
    )
    public ResponseEntity<User> deleteUser(
            Long id
    ) {
        User deletedUser = userService.deleteUser(id);

        return ResponseEntity.ok(deletedUser);
    }
}
