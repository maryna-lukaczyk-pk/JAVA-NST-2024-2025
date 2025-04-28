package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.tasks;
import com.example.projectmanagerapp.repository.tasks_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class tasks_service{

    @Autowired
    private tasks_repository tasks_repository;

    public List<tasks> getAllTasks() {
        return tasks_repository.findAll();
    }

    public tasks create_task(tasks task) {
        return tasks_repository.save(task);
    }

    public tasks update_task(Long id, tasks updatedTask) {
        if (tasks_repository.existsById(id)) {
            updatedTask.setId(id);
            return tasks_repository.save(updatedTask);
        }
        return null;
    }

    public void delete_task(Long id) {
        tasks_repository.deleteById(id);
    }
}