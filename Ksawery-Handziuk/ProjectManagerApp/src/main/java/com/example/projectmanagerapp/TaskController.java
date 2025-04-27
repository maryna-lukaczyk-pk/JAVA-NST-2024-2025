package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/{id}/priority/{level}")
    public ResponseEntity<Task> setTaskPriority(@PathVariable Long id, @PathVariable String level) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            switch (level.toUpperCase()) {
                case "HIGH" -> task.setPriority(new HighPriority());
                case "MEDIUM" -> task.setPriority(new MediumPriority());
                case "LOW" -> task.setPriority(new LowPriority());
                default -> task.setPriority(null);
            }
            taskRepository.save(task);
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
