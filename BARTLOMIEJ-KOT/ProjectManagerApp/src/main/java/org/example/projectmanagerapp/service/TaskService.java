package org.example.projectmanagerapp.service;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }


    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
}
