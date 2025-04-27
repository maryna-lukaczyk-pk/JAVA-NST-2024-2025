package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.priority.LowPriority;
import org.example.projectmanagerapp.priority.MediumPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/add")
    public Task createTask(@RequestParam String name, @RequestParam String priority) {
        PriorityLevel priorityLevel;

        switch (priority.toUpperCase()) {
            case "HIGH":
                priorityLevel = new HighPriority();
                break;
            case "MEDIUM":
                priorityLevel = new MediumPriority();
                break;
            case "LOW":
            default:
                priorityLevel = new LowPriority();
                break;
        }

        Task task = new Task(name, priorityLevel);
        return taskRepository.save(task);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
