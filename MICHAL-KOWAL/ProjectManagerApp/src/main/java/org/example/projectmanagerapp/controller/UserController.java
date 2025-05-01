package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "User controller")
@RestController
@RequestMapping("/api/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all users", description = "Gets all users")
    List<User> all() {
        return userService.getAllUsers();
    }

    @PostMapping("/")
    @Operation(summary = "Create user", description = "Creates a new user")
    User newUser(@RequestBody User newUser)
    {
        return userService.createUser(newUser);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update user", description = "Updates a user by id")
    @Parameter(name = "id", description  = "id of the user to be updated", required = true)
    ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User newUser)
    {
        User updatedUser;
        try {
            updatedUser = userService.updateById(id, newUser);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by id")
    @Parameter(name = "id", description  = "id of the user to delete", required = true)
    ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user", description = "Gets a user by id")
    @Parameter(name = "id", description  = "id of the user", required = true)
    ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user;
        try {
            user = userService.getUserById(id);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
