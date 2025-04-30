package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

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

    public Project updateProject(Long id, Project project) {
        Optional<Project> existingProject = projectRepository.findById(id);
        if (existingProject.isPresent()) {
            Project updatedProject = existingProject.get();
            updatedProject.setName(project.getName());
            updatedProject.setProjectUsers(project.getProjectUsers());
            updatedProject.setTasks(project.getTasks());
            // Zaktualizuj inne pola, jeśli to konieczne
            return projectRepository.save(updatedProject);
        }
        return null;  // lub inne podejście, np. rzucenie wyjątku
    }

    public void deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        }
    }
}
