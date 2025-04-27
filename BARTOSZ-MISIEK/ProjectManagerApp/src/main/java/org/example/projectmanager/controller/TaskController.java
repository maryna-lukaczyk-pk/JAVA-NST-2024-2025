package org.example.projectmanager.controller;

import org.example.projectmanager.dto.task.TaskCreateDto;
import org.example.projectmanager.dto.task.TaskDto;
import org.example.projectmanager.dto.task.TaskEditDto;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.service.ITaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final ITaskService service;

    public TaskController(ITaskService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public TaskDto get(@PathVariable Long id) throws EntityNotFoundException {
        return this.service.getById(id);
    }

    @PostMapping
    public Long create(@RequestBody TaskCreateDto dto) {
        return this.service.create(dto);
    }

    @PutMapping
    public void update(@RequestBody TaskEditDto dto) throws EntityNotFoundException {
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

