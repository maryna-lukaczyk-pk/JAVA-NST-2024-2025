package com.example.projectmanagerapp.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task getById(int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with id " + id));
    }

    public Task create(Task task) {
        return taskRepository.save(task);
    }

    public Task update(int id, Task task) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Task not found with id " + id);
        }
        task.setId(id);
        return taskRepository.save(task);
    }

    public void delete(int id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("Task not found with id " + id);
        }
        taskRepository.deleteById(id);
    }
}
