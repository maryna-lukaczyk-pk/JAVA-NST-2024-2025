package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.ProjectDTO;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

// Serwis zarządzający operacjami na encji projekt
@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) { this.projectRepository = projectRepository; }

    private ProjectDTO toDTO(Project p) {
        List<Long> taskId = p.getTasks() != null
                ? p.getTasks().stream().map(t -> t.getId()).collect(Collectors.toList()) : Collections.emptyList();

        List<Long> userIds = p.getProjectUsers() != null
                ? p.getProjectUsers().stream().map(pu -> pu.getUser().getId()).collect(Collectors.toList()) : Collections.emptyList();

        return new ProjectDTO(p.getId(), p.getName(), userIds, taskId);
    }

    public ProjectDTO createProject(Project project) { return toDTO(projectRepository.save(project)); }

    public List<ProjectDTO> getAllProjects() { return projectRepository.findAll().stream().map(this::toDTO).toList(); }

    public ProjectDTO getProjectById(Long id) {
        return projectRepository.findById(id).map(this::toDTO).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with ID: " + id));
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    public ProjectDTO updateProject(Long id, Project updatedProject) {
        Project existingProject = projectRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with ID: " + id));
        existingProject.setName(updatedProject.getName());
        return toDTO(projectRepository.save(existingProject));
    }
}