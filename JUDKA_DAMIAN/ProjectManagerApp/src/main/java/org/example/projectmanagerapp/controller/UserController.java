package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.schemes.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.projectmanagerapp.entity.User;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations for managing users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @Operation(summary = "Create a new user",
    description = "Create a new user in database")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody UserDTO userDTO) {
        Map<String, String> response = new HashMap<>();

        User user = new User();
        user.setUsername(userDTO.getUsername());

        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        response.put("success", "User created");
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all users",
    description = "Get a list of all users from the database")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user",
    description = "Delete a user by ID from database")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User ID")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("success", "User deleted");
            return ResponseEntity.ok(response);
        } else {
          Map<String, String> errorResponse = new HashMap<>();
          errorResponse.put("error", "User not found");
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user",
    description = "Update user attributes by ID")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "User ID")
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody UserDTO userDTO, @PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();

        if(userRepository.existsById(id)) {
            User user = new User();
            user.setId(id);
            user.setUsername(userDTO.getUsername());
            userRepository.save(user);
            response.put("success", "User updated");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
