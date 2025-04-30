package com.example.projectmanagerapp.controller;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.TasksRepository;
import com.example.projectmanagerapp.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks Controller")
public class TasksController {

    @Autowired
    private final TasksService tasksService;

    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @GetMapping("/all")
    @Operation (summary = "Get all tasks", description = "Returns list of all tasks")
    public List<Tasks> getTasks() { return tasksService.getAllTasks(); }

    @PostMapping("/create")
    @Operation( summary = "Create new task", description = "Adds new task to database")
    public ResponseEntity<Tasks> createTask(@RequestBody Tasks tasks) {
        Tasks createdTask = tasksService.createTask(tasks);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation (summary = "Update task", description = "Updates informations about task")
    public ResponseEntity<Tasks> updateTask(@PathVariable int id,@RequestBody Tasks tasks) {
        Tasks updatedTask = tasksService.updateTask(id, tasks);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

}
