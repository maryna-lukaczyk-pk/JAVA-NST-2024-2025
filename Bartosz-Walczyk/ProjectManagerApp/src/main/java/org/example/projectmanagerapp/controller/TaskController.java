package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController
{
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public ResponseEntity<List<Task>> getTasks()
    {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(taskRepository.findById(id).orElse(null));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
        @RequestBody Task task
    ) {
        task.setId(null);
        Task newTask = taskRepository.save(task);

        return ResponseEntity.ok(newTask);
    }
}
