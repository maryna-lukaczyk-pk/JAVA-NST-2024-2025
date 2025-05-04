package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

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

    // --------------------------
// Metoda do aktualizacji
// --------------------------
    public Tasks updateTask(Long id, Tasks updatedTask) {
        Tasks existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Task with id " + id + " not found."));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setTaskType(updatedTask.getTaskType());
        existingTask.setProject(updatedTask.getProject());
        existingTask.setPriorityLevel(updatedTask.getPriorityLevel());

        return taskRepository.save(existingTask);
    }

    // --------------------------
// Metoda do usuwania
// --------------------------
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException(
                    "Task with id " + id + " does not exist.");
        }
        taskRepository.deleteById(id);
    }
}
