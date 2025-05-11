package org.example.projectmanager.service;

import org.example.projectmanager.dto.project.ProjectCreateDto;
import org.example.projectmanager.dto.project.ProjectDto;
import org.example.projectmanager.dto.project.ProjectEditDto;
import org.example.projectmanager.entity.project.Project;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService implements IProjectService {
    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProjectDto> findAll() {
        return repository.findAll()
                .stream()
                .map(ProjectDto::MapFromEntity)
                .toList();
    }

    @Override
    public Long create(ProjectCreateDto dto) {
        final Project project = dto.MapToEntity();
        var created = this.repository.save(project);

        return created.getId();
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw createEntityNotFoundException(id);
        }
    }

    @Override
    public void edit(ProjectEditDto dto) throws EntityNotFoundException {
        var project = findByIdOrThrow(dto.id);
        dto.UpdateEntity(project);

        this.repository.flush();
    }

    @Override
    public ProjectDto getById(Long id) throws EntityNotFoundException {
        var project = findByIdOrThrow(id);
        return ProjectDto.MapFromEntity(project);
    }

    private Project findByIdOrThrow(Long id) throws EntityNotFoundException {
        return this.repository.findById(id).orElseThrow(() -> createEntityNotFoundException(id));
    }

    private static EntityNotFoundException createEntityNotFoundException(Long id) {
        return new EntityNotFoundException(String.format("Project by the id %d has not been found", id));
    }
}
