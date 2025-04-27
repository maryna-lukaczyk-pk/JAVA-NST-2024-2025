package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project getProjectById(Integer id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.orElse(null);
    }

    public Project updateProject(Integer id, Project project) {
        Optional<Project> existingProject = projectRepository.findById(id);
        if (existingProject.isPresent()) {
            project.setId(id);
            return projectRepository.save(project);
        }
        return null;
    }

    public void deleteProject(Integer id) {
        projectRepository.deleteById(id);
    }
}