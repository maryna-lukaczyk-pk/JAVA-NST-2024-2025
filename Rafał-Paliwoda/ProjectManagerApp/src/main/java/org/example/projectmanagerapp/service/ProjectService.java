package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final UserRepository userRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, TaskService taskService, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Projekt o ID " + id + " nie został znaleziony"));

        project.setName(projectDetails.getName());
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Projekt o ID " + id + " nie został znaleziony"));

        taskRepository.deleteByProjectId(id);
        projectRepository.delete(project);
    }

    public List<Task> getProjectTasks(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Projekt o ID " + projectId + " nie został znaleziony");
        }
        return taskService.findTasksByProjectId(projectId);
    }


    public void assignUserToProject(Long projectId, Long userId) {
        Project project = this.findProjectById(projectId).orElseThrow();

        User user = userRepository.findById(userId).orElseThrow();

        if (project.getUsers() == null) {
            project.setUsers(new ArrayList<>());
        }

        if (!project.getUsers().contains(user)) {
            project.getUsers().add(user);
            projectRepository.save(project);
        }
    }
}