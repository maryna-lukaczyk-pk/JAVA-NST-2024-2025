package org.example.projectmanager.controller;

import org.example.projectmanager.dto.user.UserCreateDto;
import org.example.projectmanager.dto.user.UserDto;
import org.example.projectmanager.dto.user.UserEditDto;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService service;

    public UserController(IUserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) throws EntityNotFoundException {
        return this.service.getById(id);
    }

    @PostMapping
    public Long create(@RequestBody UserCreateDto dto) {
        return this.service.create(dto);
    }

    @PutMapping
    public void update(@RequestBody UserEditDto dto) throws EntityNotFoundException {
        this.service.edit(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        this.service.delete(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
