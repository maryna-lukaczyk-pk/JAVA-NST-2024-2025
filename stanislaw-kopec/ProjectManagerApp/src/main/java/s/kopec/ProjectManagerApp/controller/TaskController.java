package s.kopec.ProjectManagerApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import s.kopec.ProjectManagerApp.entity.Task;
import s.kopec.ProjectManagerApp.repository.TaskRepository;
import s.kopec.ProjectManagerApp.service.TaskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operation for mapping tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("")
    @Operation(summary = "Retrieve all tasks", description = "Returns a list of all tasks from the database")
    public List<Task> getAll() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a task by ID", description = "Returns a task by its ID")
    public Optional<Task> getById(
            @Parameter(description = "ID of the task to retrieve", required = true)
            @PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new task", description = "Adds a new task to the database")
    public Task create(
            @Parameter(description = "Task object to create", required = true)
            @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a task by ID", description = "Deletes a task from the database using its ID")
    public void deleteById(
            @Parameter(description = "ID of the task to delete", required = true)
            @PathVariable Long id) {
        taskService.deleteTaskById(id);
    }

    @PutMapping("/update/{id}/{newTaskTitle}")
    @Operation(summary = "Update task title", description = "Updates the title of an existing task")
    public void update(
            @Parameter(description = "ID of the task to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "New title for the task", required = true)
            @PathVariable String newTitle) {
        taskService.updateTaskTitle(id,newTitle);
    }

}
