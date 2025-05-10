package org.example.projectmanagerapp.services;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepo) {this.taskRepository = taskRepo;}

    public List<Tasks> getAllTasks() {return taskRepository.findAll();}

    public Tasks createTask(Tasks task) {return taskRepository.save(task);}

    public void deleteTask(long taskId) {taskRepository.deleteById(taskId);}

    public Tasks updateTask(long taskId, Tasks updatedTask) {
        Tasks existingTask = taskRepository.findById(taskId).orElseThrow(()->new RuntimeException("Task not found"));
        existingTask.setProject(updatedTask.getProject());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setTaskType(updatedTask.getTaskType());
        existingTask.setTitle(updatedTask.getTitle());
        return taskRepository.save(existingTask);
    }
}