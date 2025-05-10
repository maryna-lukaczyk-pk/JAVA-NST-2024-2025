package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task API")
public class TaskController {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElse(null);

        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        return ResponseEntity.ok(task);
    }


    @PostMapping("/add")
    public ResponseEntity<Task> createProject(@RequestBody Task task) {
        Task createdTask = taskRepository.save(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task taskBody) {
        Task task =  taskRepository.findById(taskId)
                .map(existingTask -> {
                    if (taskBody.getTitle() != null) {
                        existingTask.setTitle(taskBody.getTitle());
                    }
                    if (taskBody.getDescription() != null) {
                        existingTask.setDescription(taskBody.getDescription());
                    }
                    if (taskBody.getTaskType() != null) {
                        existingTask.setTaskType(taskBody.getTaskType());
                    }

                    return taskRepository.save(existingTask);
                })
                .orElse(null);

        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Operation was not successful");
        }

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/remove/{taskId}")
    public ResponseEntity<Object> deleteTask(@PathVariable Long taskId) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    taskRepository.delete(task);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
