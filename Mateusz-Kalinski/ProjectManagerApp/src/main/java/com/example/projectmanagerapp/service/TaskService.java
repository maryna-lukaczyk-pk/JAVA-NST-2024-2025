package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Tasks;
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

    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    public Tasks createTask(Tasks task) {
        return taskRepository.save(task);
    }

    public Optional<Tasks> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Tasks updateTask(Long id, Tasks taskDetails) {
        if (taskRepository.existsById(id)) {
            taskDetails.setId(id);
            return taskRepository.save(taskDetails);
        }
        return null;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}