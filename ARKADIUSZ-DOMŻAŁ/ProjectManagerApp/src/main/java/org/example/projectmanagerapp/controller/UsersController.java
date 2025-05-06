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
}
