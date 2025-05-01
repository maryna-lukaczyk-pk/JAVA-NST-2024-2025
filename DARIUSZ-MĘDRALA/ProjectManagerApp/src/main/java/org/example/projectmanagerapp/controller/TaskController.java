package org.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Task API", description = "API for managing tasks")
@RestController
@RequestMapping("/api/task")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Operation(summary = "Get all tasks", description = "Retrieve a list of all tasks")
    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Operation(summary = "Create a new task", description = "Create a new task with the provided details")
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }
}
