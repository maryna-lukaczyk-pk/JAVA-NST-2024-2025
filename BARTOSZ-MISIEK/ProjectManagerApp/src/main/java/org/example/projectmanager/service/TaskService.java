package org.example.projectmanager.service;

import org.example.projectmanager.dto.task.TaskCreateDto;
import org.example.projectmanager.dto.task.TaskDto;
import org.example.projectmanager.dto.task.TaskEditDto;
import org.example.projectmanager.entity.task.Task;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService implements ITaskService {
    public TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TaskDto> findAll() {
        return this.repository.findAll().stream()
                .map(TaskDto::MapFromEntity)
                .toList();
    }

    @Override
    public Long create(TaskCreateDto dto) {
        var task = dto.MapToEntity();
        var created = this.repository.save(task);

        return created.getId();
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        }

        throw createEntityNotFoundException(id);
    }

    @Override
    public void edit(TaskEditDto dto) throws EntityNotFoundException {
        var task = findByIdOrThrow(dto.id);
        dto.UpdateEntity(task);

        this.repository.flush();
    }

    @Override
    public TaskDto getById(Long id) throws EntityNotFoundException {
        var task = findByIdOrThrow(id);

        return TaskDto.MapFromEntity(task);
    }

    private Task findByIdOrThrow(Long id) throws EntityNotFoundException {
        return this.repository.findById(id).orElseThrow(() -> createEntityNotFoundException(id));
    }

    private static EntityNotFoundException createEntityNotFoundException(Long id) {
        return new EntityNotFoundException(String.format("Task by the id %d has not been found", id));
    }
}
