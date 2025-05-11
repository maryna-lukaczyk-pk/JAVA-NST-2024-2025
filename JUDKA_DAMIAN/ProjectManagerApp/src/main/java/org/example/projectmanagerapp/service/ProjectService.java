package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.schemas.ProjectDTO;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public Project findProjectById(Integer id) throws NotFoundException {
        return projectRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public void addProject(ProjectDTO dto) {
        Project project = new Project();
        project.setName(dto.getName());

        projectRepository.save(project);
    }

    public void deleteProject(int id) throws NotFoundException {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new NotFoundException();
        }
    }

    public void updateProject(ProjectDTO dto, int id) throws NotFoundException {
        if(projectRepository.existsById(id)) {
            Project project = new Project();
            project.setId(id);
            project.setName(dto.getName());
            projectRepository.save(project);
        } else {
            throw new NotFoundException();
        }
    }
}

