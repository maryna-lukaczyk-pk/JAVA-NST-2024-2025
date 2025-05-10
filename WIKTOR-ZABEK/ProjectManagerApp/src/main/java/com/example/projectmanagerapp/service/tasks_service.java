package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.tasks;
import com.example.projectmanagerapp.repository.tasks_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class tasks_service {

    @Autowired
    private tasks_repository tasks_repository;

    public List<tasks> getAllTasks() {
        return tasks_repository.findAll();
    }

    public tasks create_task(tasks task) {
        return tasks_repository.save(task);
    }

    // Find task by ID
    public Optional<tasks> getTaskById(Long id) {
        return tasks_repository.findById(id);
    }

    // Delete task by ID
    public void deleteTaskById(Long id) {
        tasks_repository.deleteById(id);
    }

    // Update task by ID
    public tasks updateTask(Long id, tasks updatedTask) {
        Optional<tasks> existingTask = tasks_repository.findById(id);
        if (existingTask.isPresent()) {
            tasks task = existingTask.get();
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setTaskType(updatedTask.getTaskType());
            task.setPriority(updatedTask.getPriority());
            task.setProject_id(updatedTask.getProject_id());
            task.setProjects(updatedTask.getProjects());
            return tasks_repository.save(task);
        }
        return null; // Or throw an exception indicating task not found
    }
}