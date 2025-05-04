package com.example.projectmanagerapp.services;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepo) {
        this.projectRepository = projectRepo;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(long projectId) {
        projectRepository.deleteById(projectId);
    }

    public Project updateProject(long projectId, Project updatedProject) {
        Project existingProject = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        existingProject.setName(updatedProject.getName());
        return projectRepository.save(existingProject);
    }

    public Project getProjectById(long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
    }
}
