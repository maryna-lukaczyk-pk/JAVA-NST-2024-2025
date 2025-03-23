package org.example.projectmanagerapp.controller;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/Tasks")
public class TasksController {
    private final TaskRepository taskRepository;

    public TasksController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Tasks> createTask(@RequestBody Tasks task) {
        Tasks savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }
}
