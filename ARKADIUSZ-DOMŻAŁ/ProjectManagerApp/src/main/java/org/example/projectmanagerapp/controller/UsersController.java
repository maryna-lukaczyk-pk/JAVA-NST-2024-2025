package org.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;

@RestController
@RequestMapping("api/users")
@Tag(name="Users")
public class UsersController {

    private final UserRepository userRepository;
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "Retrieve all Users", description = "Returns a list of all Users")
    @GetMapping("/get")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Create a new User", description = "Allows to create a new User")
    @PostMapping("/create")
    public ResponseEntity<Users> createUser(@RequestBody @Parameter(description="User object that needs to be created") Users user) {
        Users savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}
