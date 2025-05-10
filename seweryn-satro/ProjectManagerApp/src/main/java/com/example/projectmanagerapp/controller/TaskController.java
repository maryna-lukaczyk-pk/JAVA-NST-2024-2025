package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@Tag(name = "Tasks", description = "Managing tasks")
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    @Operation(summary = "Project task", description = "Returns the list of every task")
    public List<Task> allTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Add new task", description = "Add new task to the database")
    public Task newTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Update task data based on task ID")
    public Task updateTask(
            @Parameter(description = "Task ID")
            @PathVariable Integer id,
            @RequestBody Task newTask) {

        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(newTask.getTitle());
                    task.setDescription(newTask.getDescription());
                    task.setTask_type(newTask.getTask_type());
                    task.setPriority(newTask.getPriority());
                    task.setProject(newTask.getProject());
                    return taskRepository.save(task);
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Delete task data based on task ID")
    public void deleteTask(
            @Parameter(description = "Task ID", required = true)
            @PathVariable Integer id) {

        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        }
    }

}
