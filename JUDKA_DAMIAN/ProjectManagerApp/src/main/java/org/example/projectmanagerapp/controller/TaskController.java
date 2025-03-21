package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
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
public class TaskController {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public TaskController(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    @PostMapping
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
        task.setTaskType(TaskType.valueOf(taskDTO.getTaskType()));
        task.setProject(optionalProject.get());

        Task savedTask = taskRepository.save(task);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedTask.getId()).toUri();

        response.put("success", String.valueOf(true));
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @DeleteMapping("/{id}")
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
                task.setTaskType(TaskType.valueOf(taskDTO.getTaskType()));
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
