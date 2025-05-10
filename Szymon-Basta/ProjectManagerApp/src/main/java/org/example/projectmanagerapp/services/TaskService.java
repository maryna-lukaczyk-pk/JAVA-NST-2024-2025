package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.util.List;

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

    public Tasks updateTask(Long id, Tasks updatedTask) {
        Optional<Tasks> existingTask = taskRepository.findById(id);

        if (existingTask.isPresent()) {
            Tasks task = existingTask.get();
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setTaskType(updatedTask.getTaskType());
            task.setProjects(updatedTask.getProjects());
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found id: " + id);
        }
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public Tasks getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found id: " + id));
    }

}
