package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.interfaces.TaskRequestDTO;
import org.example.projectmanagerapp.interfaces.TaskResponseDTO;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToTaskResponseDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        return mapToTaskResponseDTO(task);
    }

    public TaskResponseDTO createTask(TaskRequestDTO taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setTaskType(taskRequest.getTaskType());
        Project project = projectRepository.findById(taskRequest.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        task.setProject(project);
        Task savedTask = taskRepository.save(task);
        return mapToTaskResponseDTO(savedTask);
    }

    public TaskResponseDTO updateTask(Long taskId, Task taskBody) {
        Task task = taskRepository.findById(taskId)
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Operation was not successful"));
        return mapToTaskResponseDTO(task);
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        taskRepository.delete(task);
    }

    private TaskResponseDTO mapToTaskResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setTaskType(task.getTaskType());
        return dto;
    }
}
