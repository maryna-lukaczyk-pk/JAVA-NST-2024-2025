package org.jerzy.projectmanagerapp.controller;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.Task;
import org.jerzy.projectmanagerapp.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository repository) {
        this.taskRepository = repository;
    }

    @GetMapping
    public List<Task> get() {
        return taskRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Task> post(@RequestBody Task task) {
        Task newTask = taskRepository.save(task);
        
        return new ResponseEntity<>(newTask, HttpStatus.OK);
    }
}
