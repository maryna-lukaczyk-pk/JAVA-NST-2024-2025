package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.service.UserService;
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
    public Users getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Create a new User.")
    @ApiResponse(responseCode = "200", description = "User created successfully.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Users.class)))
    @PostMapping
    public Users createUser(@RequestBody Users users) {
        return userService.createUser(users);
    }
}
