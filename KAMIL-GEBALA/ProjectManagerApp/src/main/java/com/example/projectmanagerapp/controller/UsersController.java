package com.example.projectmanagerapp.controller;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;


@RestController
@RequestMapping("/api/users")
@Tag(name = "Users Controller")
public class UsersController {
    @Autowired
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/all")
    @Operation  (summary = "Get all users",description = "Returns list of all users")
    public List<Users> getUsers() { return usersService.getAllUsers(); }

    @PostMapping("/create")
    @Operation  (summary = "Create new user",description = "Adds new user to database")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        Users createdUser = usersService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation (summary = "Update user", description = "Updates informations about user")
    public ResponseEntity<Users> updateUser(@PathVariable int id, @RequestBody Users user) {
        Users updatedUser = usersService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
