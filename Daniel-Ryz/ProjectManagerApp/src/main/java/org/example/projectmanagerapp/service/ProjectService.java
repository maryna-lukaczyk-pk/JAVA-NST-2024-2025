package org.example.projectmanagerapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.CreateProjectRequest;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        List<Project> project = projectRepository.findAll();
        if (project.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return project;
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    @Transactional
    public Project createProject(CreateProjectRequest request) {
        Project project = new Project();
        project.setName(request.name());
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(Long Id, CreateProjectRequest request) {
        Project project = getProjectById(Id);
        project.setName(request.name());
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

}
