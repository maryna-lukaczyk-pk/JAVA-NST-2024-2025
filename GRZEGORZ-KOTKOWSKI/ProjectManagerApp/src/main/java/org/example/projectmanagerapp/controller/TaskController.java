package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    public Tasks createTask(@RequestBody Tasks task) {
        return taskRepository.save(task);
    }

}