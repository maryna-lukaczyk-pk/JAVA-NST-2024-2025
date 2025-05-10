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

    public Optional<Project> getProjectById(Integer id) {
        return projectRepository.findById(id);
    }

    public void deleteProjectById(Integer id) {
        projectRepository.deleteById(id);
    }

    public Project updateProject(Integer id, Project projectDetails) {
        Optional<Project> project = projectRepository.findById(id);
        if(project.isPresent()) {
            Project existingProject = project.get();
            existingProject.setName(projectDetails.getName());
            return projectRepository.save(existingProject);
        }
        return null;
    }
}