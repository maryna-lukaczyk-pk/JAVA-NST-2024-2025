package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Projects> getAllProjects() {
        return projectRepository.findAll();
    }

    public Projects createProject(Projects project) {
        return projectRepository.save(project);
    }

    public Projects updateProject(Long id, Projects updatedProject) {
        Optional<Projects> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Projects existingProject = optionalProject.get();
            existingProject.setName(updatedProject.getName());
            return projectRepository.save(existingProject);
        } else {
            throw new IllegalArgumentException("Project not found id: " + id);
        }
    }

    public void deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Project not found id: " + id);
        }
    }
}
