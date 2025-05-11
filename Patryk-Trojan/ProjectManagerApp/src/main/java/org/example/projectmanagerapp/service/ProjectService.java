package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    public List<Project> getAllProjects() {
        return this.projectRepository.findAll();
    }
    public Project createProject(Project project) {
        return this.projectRepository.save(project);
    }
    public Project getProjectById(Long id) {
        return this.projectRepository.findById(id).orElse(null);
    }
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
    public void updateProject(Long id, Project project) {
        Project projcetToUpdate = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        projcetToUpdate.setName(project.getName());
        this.projectRepository.save(projcetToUpdate);
    }
}
