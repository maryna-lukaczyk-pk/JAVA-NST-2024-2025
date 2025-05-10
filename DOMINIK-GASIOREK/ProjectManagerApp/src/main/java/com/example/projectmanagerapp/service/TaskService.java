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

    public Optional<Task> update(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setTask_type(updatedTask.getTask_type());
            task.setProject(updatedTask.getProject());
            return taskRepository.save(task);
        });
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
