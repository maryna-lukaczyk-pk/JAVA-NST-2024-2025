package org.example.projectmanagerapp.service;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        // Aktualizujemy tylko możliwe do zmiany pola
        if (projectDetails.getName() != null) {
            project.setName(projectDetails.getName());
        }
        // Uwaga: Nie aktualizujemy bezpośrednio listy tasks ani users
        // aby uniknąć niekontrolowanych zmian w relacjach

        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        // Sprawdzamy czy projekt nie ma przypisanych zadań
        if (project.getTasks() != null && !project.getTasks().isEmpty()) {
            throw new IllegalStateException("Cannot delete project with assigned tasks");
        }

        // Usuwamy powiązania z użytkownikami
        if (project.getUsers() != null) {
            new HashSet<>(project.getUsers()).forEach(user -> {
                user.getProjects().remove(project);
                project.getUsers().remove(user);
            });
        }

        projectRepository.delete(project);
    }

    public Optional<Project> getProjectById(Long id) {
        try {
            return projectRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving project with id: " + id, e);
        }
    }
}
