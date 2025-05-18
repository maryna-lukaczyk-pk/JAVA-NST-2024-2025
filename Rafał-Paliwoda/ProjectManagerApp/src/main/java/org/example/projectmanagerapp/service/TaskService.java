package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Zadanie o ID " + id + " nie zostało znalezione"));

        task.setDescription(taskDetails.getDescription());
        task.setTitle(taskDetails.getTitle());
        task.setTaskType(taskDetails.getTaskType());
        task.setPriorityLevel(taskDetails.getPriorityLevel());

        if (taskDetails.getProject() != null && taskDetails.getProject().getId() != null) {
            Project project = projectRepository.findById(taskDetails.getProject().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Projekt o ID " + taskDetails.getProject().getId() + " nie został znaleziony"));
            task.setProject(project);
        }

        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Zadanie o ID " + id + " nie zostało znalezione"));

        taskRepository.delete(task);
    }
}