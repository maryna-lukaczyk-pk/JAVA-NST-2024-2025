package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.schemes.TaskDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operations for managing tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskController(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @PostMapping
    @Operation(summary = "Create a new task",
            description = "Create a new task in database")
    public ResponseEntity<Map<String, String>> createTask(@RequestBody TaskDTO taskDTO) {
        Optional<Project> optionalProject = projectRepository.findById(taskDTO.getProjectId());

        Map<String, String> response = new HashMap<>();

        if(optionalProject.isEmpty()) {
            response.put("error", "Project not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setTaskType(taskDTO.getTaskType());
        task.setProject(optionalProject.get());

        Task savedTask = taskRepository.save(task);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedTask.getId()).toUri();

        response.put("success", String.valueOf(true));
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all tasks",
    description = "Get a list of all tasks from the database")
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task",
    description = "Delete a task by ID from database")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Task ID")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Integer id) {
        if(taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("success", "Task deleted");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Task not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update task attributes",
    description = "Update task attribute/attributes by ID")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Task ID")
    public ResponseEntity<Map<String, String>> updateProject(@RequestBody TaskDTO taskDTO, @PathVariable Integer id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Map<String, String> response = new HashMap<>();

        if(optionalTask.isPresent()) {
            Task task = optionalTask.get();

            if(taskDTO.getTitle() != null) {
                task.setTitle(taskDTO.getTitle());
            }
            if(taskDTO.getDescription() != null) {
                task.setDescription(taskDTO.getDescription());
            }
            if(taskDTO.getTaskType() != null) {
                task.setTaskType(taskDTO.getTaskType());
            }

            taskRepository.save(task);
            response.put("success", "Task updated");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Task not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
