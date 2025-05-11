package org.example.projectmanager.service;

import org.example.projectmanager.dto.project.ProjectCreateDto;
import org.example.projectmanager.dto.project.ProjectDto;
import org.example.projectmanager.dto.project.ProjectEditDto;
import org.example.projectmanager.exception.EntityNotFoundException;

import java.util.List;

public interface IProjectService {
    /**
     * Returns the list of all entities
     */
    List<ProjectDto> findAll();

    /**
     * Creates an entity based on the provided dto object
     *
     * @return Returns the id of the created entity
     */
    Long create(ProjectCreateDto dto);

    void delete(Long id) throws EntityNotFoundException;
    void edit(ProjectEditDto dto) throws EntityNotFoundException;
    ProjectDto getById(Long id) throws EntityNotFoundException;
}
