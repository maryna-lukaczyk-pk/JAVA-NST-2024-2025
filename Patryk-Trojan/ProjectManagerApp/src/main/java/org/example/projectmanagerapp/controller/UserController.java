package org.example.projectmanagerapp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Users", description = "Operations for managing project")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping
    @Operation(summary = "Return all Users", description = "Return a list of all users from DB")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Return a User", description = "Return a user by id from DB")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }
    @PostMapping
    @Operation(summary = "Create a User", description = "Create a user by id from DB")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update a User", description = "Update a user by id from DB")
    public void updateUser(@RequestBody User user, @PathVariable Long id) {
         userService.updateUser(id, user);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a User", description = "Deleting a user by id from DB")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
    @DeleteMapping()
    @Operation(summary = "Delete all Users", description = "Deleting all users  from DB")
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }
}
