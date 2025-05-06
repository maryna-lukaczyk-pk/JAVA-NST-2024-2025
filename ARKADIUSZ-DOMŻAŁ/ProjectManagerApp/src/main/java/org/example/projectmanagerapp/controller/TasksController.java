package org.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.example.projectmanagerapp.service.TaskService;


import java.util.List;
@RestController
@RequestMapping("api/tasks")
@Tag(name="Tasks")
public class TasksController {
    private final TaskService taskService;

    public TasksController(TaskService taskService)  {
        this.taskService = taskService;
    }

    @Operation(summary = "Retrieve all Tasks", description = "Returns a list of Tasks")
    @GetMapping("/get")
    public List<Tasks> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Operation(summary = "Create a new Task", description = "Allows to create a new Task")
    @PostMapping("/create")
    public ResponseEntity<Tasks> createTask(@RequestBody @Parameter(description="Task object that needs to be created") Tasks task) {
        Tasks createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }
}
