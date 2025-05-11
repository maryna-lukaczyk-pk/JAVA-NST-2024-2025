package org.example.projectmanager.service;

import org.example.projectmanager.entity.Tasks;
import org.example.projectmanager.repository.TasksRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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

    public Tasks updateTasks(Long id, Tasks tasksDetails) {
        Tasks tasks = tasksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        tasks.setTitle(tasksDetails.getTitle());
        tasks.setDescription(tasksDetails.getDescription());

        return tasksRepository.save(tasks);
    }

    public void deleteTasks(Long id) {
        Tasks tasks = tasksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tasks not found with id: " + id));

        tasksRepository.delete(tasks);
    }

    public Optional<Tasks> getTasksById(Long id) {
        return tasksRepository.findById(id);
    }
}