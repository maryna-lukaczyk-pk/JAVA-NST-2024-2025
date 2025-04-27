package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.repozytorium.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskService {
    private final TasksRepository tasksRepository;

    @Autowired
    public TaskService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }
    public Tasks getTaskById(Long id) {
        Optional<Tasks> task = tasksRepository.findById(id);
        return task.orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    public Tasks createTask(Tasks tasks) {
        return tasksRepository.save(tasks);
    }
    public Tasks updateTask(Long id, Tasks taskDetails) {
        return tasksRepository.findById(id)
                .map(task -> {
                    task.setTitle(taskDetails.getTitle());
                    task.setDescription(taskDetails.getDescription());
                    return tasksRepository.save(task);
                })
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
    }

    public void deleteTask(Long id) {
        if (tasksRepository.existsById(id)) {
            tasksRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Task not found with id: " + id);
        }
    }
}
