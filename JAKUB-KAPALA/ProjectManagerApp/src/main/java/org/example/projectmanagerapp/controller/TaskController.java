package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.interfaces.TaskResponseDTO;
import org.example.projectmanagerapp.service.TaskService;
import org.example.projectmanagerapp.interfaces.TaskRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task API")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public List<TaskResponseDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long taskId) {
        TaskResponseDTO task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/add")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequest) {
        TaskResponseDTO createdTask = taskService.createTask(taskRequest);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long taskId, @RequestBody Task taskBody) {
        TaskResponseDTO task = taskService.updateTask(taskId, taskBody);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/remove/{taskId}")
    public ResponseEntity<Object> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
