package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // GET
    @GetMapping
    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    // POST
    @PostMapping
    public Tasks createTask(@RequestBody Tasks task) {
        return taskRepository.save(task);
    }
}
