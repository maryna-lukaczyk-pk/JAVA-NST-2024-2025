package org.example.projectmanager.controller;

import org.example.projectmanager.entity.Tasks;
import org.example.projectmanager.repository.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/tasks")
@Tag (
    name = "Tasks",
    description = "TasksController"
)
public class TasksController {
    @Autowired
    private TasksRepository tasksRepository;

    @GetMapping
    public List<Tasks> getAllTasks() {
        return tasksRepository.findAll();
    }

    @PostMapping
    public Tasks createTask(@RequestBody Tasks task) {
        return tasksRepository.save(task);
    }
}