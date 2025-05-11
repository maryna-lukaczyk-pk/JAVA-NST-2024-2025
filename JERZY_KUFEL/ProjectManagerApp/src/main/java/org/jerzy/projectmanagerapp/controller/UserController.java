package org.jerzy.projectmanagerapp.controller;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.User;
import org.jerzy.projectmanagerapp.repository.UserRepository;
import org.jerzy.projectmanagerapp.service.TaskService;
import org.jerzy.projectmanagerapp.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(name = "User", description = "User managment methods")
@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService service;

  public UserController(UserRepository repository, TaskService taskService) {
    this.service = new UserService(repository);
  }

  @Operation(summary = "List all users")
  @GetMapping
  public List<User> get() {
    return this.service.getAllUsers();
  }

  @Operation(summary = "Get a specific user by id")
  @GetMapping("/{id}")
  public User getUserById(@Parameter(description = "User id") String id) {
    return this.service.getById(id);
  }

  @Operation(summary = "Create new user")
  @PostMapping("/create")
  public ResponseEntity<User> postMethodName(@RequestBody User user) {
    return new ResponseEntity<>(this.service.create(user), HttpStatus.CREATED);
  }

  @Operation(summary = "Update existing user")
  @PutMapping("path/{id}")
  public ResponseEntity<User> updateUser(@Parameter(description = "User id") String id, @RequestBody User user) {
    try {
      User updatedUser = this.service.update(id, user);
      return ResponseEntity.ok(updatedUser);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Delete existing user")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@Parameter(description = "User id") String id) {
    try {
      this.service.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
