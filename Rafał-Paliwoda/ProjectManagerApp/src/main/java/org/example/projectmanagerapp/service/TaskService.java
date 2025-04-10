package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public Task createTask(Task task) {
        if (task.getProject().getId() != null) {
            Project project = projectRepository.findById(task.getProject().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Projekt o ID " + task.getProject().getId() + " nie został znaleziony"));
            task.setProject(project);
        }
        return taskRepository.save(task);
    }

    public List<Task> findTasksByProjectId(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Projekt o ID " + projectId + " nie został znaleziony");
        }
        return taskRepository.findByProjectId(projectId);
    }

    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
}