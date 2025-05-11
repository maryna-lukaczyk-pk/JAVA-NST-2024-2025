package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(
        TaskRepository taskRepository
    ) {
        this.taskRepository = taskRepository;
    }

    public Task getTaskById(
        Long id
    ) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getAllTasks()
    {
        return taskRepository.findAll();
    }

    public Task createTask(
        Task task
    ) {
        return taskRepository.save(task);
    }

    public Task updateTask(
        Task taskData
    ) {
        Task task = taskRepository.findById(taskData.getId()).orElse(null);

        if (task != null) {
            BeanUtils.copyProperties(taskData, task);
            taskRepository.save(task);
        }

        return task;
    }

    public Task deleteTask(
        Long id
    ) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task != null) {
            taskRepository.delete(task);
        }

        return task;
    }
}
