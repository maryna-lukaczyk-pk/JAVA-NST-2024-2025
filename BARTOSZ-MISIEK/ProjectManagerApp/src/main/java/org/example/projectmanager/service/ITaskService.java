package org.example.projectmanager.service;

import org.example.projectmanager.dto.task.TaskCreateDto;
import org.example.projectmanager.dto.task.TaskDto;
import org.example.projectmanager.dto.task.TaskEditDto;
import org.example.projectmanager.exception.EntityNotFoundException;

import java.util.List;

public interface ITaskService {
    /**
     * Returns the list of all entities
     */
    List<TaskDto> findAll();

    /**
     * Creates an entity based on the provided dto object
     *
     * @return Returns the id of the created entity
     */
    Long create(TaskCreateDto dto);

    void delete(Long id) throws EntityNotFoundException;
    void edit(TaskEditDto dto) throws EntityNotFoundException;
    TaskDto getById(Long id) throws EntityNotFoundException;
}
