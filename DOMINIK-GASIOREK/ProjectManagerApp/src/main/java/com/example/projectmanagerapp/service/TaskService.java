package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Optional<Task> getByID(Long id) {
        return taskRepository.findById(id);
    }

    public Task create(Task newTask) {
        return taskRepository.save(newTask);
    }
}
