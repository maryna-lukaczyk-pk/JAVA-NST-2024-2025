package org.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.http.HttpStatus;


import java.util.List;

@RestController
@RequestMapping("api/users")
@Tag(name="Users")
public class UsersController {

    private final UserService userService;
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Retrieve all Users", description = "Returns a list of all Users")
    @GetMapping("/get")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Create a new User", description = "Allows to create a new User")
    @PostMapping("/create")
    public ResponseEntity<Users> createUser(@RequestBody @Parameter(description="User object that needs to be created") Users user) {
        Users createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Update the User", description = "Updates an existing User by Id")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(
            @Parameter(description = "The ID of the User to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated User object", required = true)
            @RequestBody Users updatedUser) {

        return userService.updateUser(id, updatedUser)
                .map(updated -> ResponseEntity.ok("User updated successfully"))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with id: " + id + " not found"));
    }

    @Operation(summary = "Delete a User", description = "Deletes a User by Id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "ID of the User to delete", required = true)
            @PathVariable Long id) {

        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    //Wyszukanie uzytkownika po ID
    @Operation(summary = "Get a User by ID", description = "Returns a single User by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(
            @Parameter(description = "The ID of the User to retrieve", required = true)
            @PathVariable Long id) {

        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok("User found: " + user.getUsername()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with id: " + id + " not found"));
    }
}
