package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Task", description = "Task controller")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all tasks", description = "Gets all tasks")
    List<Task> all() {
        return taskService.getAllTasks();
    }

    @PostMapping("/")
    @Operation(summary = "Create task", description = "Creates a new task")
    Task newTask(@RequestBody Task newTask)
    {
        return taskService.createTask(newTask);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update task", description = "Updates a task by id")
    @Parameter(name = "id", description  = "id of the task to be updated", required = true)
    ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task newTask)
    {
        Task updatedTask;
        try {
            updatedTask = taskService.updateById(id, newTask);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete task", description = "Deletes a task by id")
    @Parameter(name = "id", description  = "id of the task to delete", required = true)
    ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task", description = "Gets a task by id")
    @Parameter(name = "id", description  = "id of the task", required = true)
    ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task;
        try {
            task = taskService.getTaskById(id);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
