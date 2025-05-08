package com.example.projectmanagerapp.services;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
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

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }

    public Project updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id).map(project -> {
            project.setName(updatedProject.getName());
            return projectRepository.save(project);
        }).orElseThrow(() -> new RuntimeException("Project not found"));
    }

}
