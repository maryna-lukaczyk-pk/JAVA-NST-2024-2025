package com.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
            taskRepository.save(task); // Aktualizujemy w bazie
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
