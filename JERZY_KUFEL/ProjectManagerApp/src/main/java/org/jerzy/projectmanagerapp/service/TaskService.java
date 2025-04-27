package org.jerzy.projectmanagerapp.service;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.Task;
import org.jerzy.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository repository) {
        this.taskRepository = repository;
    }

    public List<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    public Task create(Task task) {
        return this.taskRepository.save(task);
    }
}
