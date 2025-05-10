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
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Project does not exist"));
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProjectById(Long id, Project project) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project with id " + id + " does not exist");
        }

        project.setId(id);
        return projectRepository.save(project);
    }

    public Boolean deleteProjectById(Long id) {
        if (!projectRepository.existsById(id)) {
            return false;
        }

        projectRepository.deleteById(id);
        return true;
    }
}
