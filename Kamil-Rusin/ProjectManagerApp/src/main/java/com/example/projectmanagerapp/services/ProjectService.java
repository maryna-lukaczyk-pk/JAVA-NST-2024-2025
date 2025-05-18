package com.example.projectmanagerapp.services;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    public final ProjectRepository projectRepository;
    public final UserRepository userRepository;
    public final TaskRepository taskRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project project) {
        return projectRepository.findById(id)
                .map(existingProject -> {
                    existingProject.setName(project.getName());
                    existingProject.setUsers(project.getUsers());
                    existingProject.setTasks(project.getTasks());
                    return projectRepository.save(existingProject);
                })
                .orElseGet(() -> projectRepository.save(project));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public void assignUserToProject(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project with id " + projectId + " not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        project.getUsers().add(user);
        user.getProjects().add(project);

        projectRepository.save(project);
        userRepository.save(user);
    }

    public void assignTaskToProject(Long taskId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project with id " + projectId + " not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task with id " + taskId + " not found"));

        project.getTasks().add(task);
        task.setProjectId(project);

        projectRepository.save(project);
        taskRepository.save(task);
    }
}


