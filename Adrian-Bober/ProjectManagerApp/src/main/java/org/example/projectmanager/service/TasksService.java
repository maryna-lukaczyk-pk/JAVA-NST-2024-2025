package org.example.projectmanager.service;

import org.example.projectmanager.entity.Tasks;
import org.example.projectmanager.repository.TasksRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TasksService {
    private final TasksRepository tasksRepository;

    public TasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public List<Tasks> getAllTasks() {
        return tasksRepository.findAll();
    }

    public Tasks createTask(Tasks tasks) {
        return tasksRepository.save(tasks);
    }
}
