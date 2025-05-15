package org.example.projectmanager.service;

import jakarta.transaction.Transactional;
import org.example.projectmanager.entity.Project;
import org.example.projectmanager.entity.Tasks;
import org.example.projectmanager.repository.ProjectRepository;
import org.example.projectmanager.repository.TasksRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TasksService {
    private final TasksRepository tasksRepository;
    private final ProjectRepository projectRepository;

    public TasksService(TasksRepository tasksRepository, ProjectRepository projectRepository) {
        this.tasksRepository = tasksRepository;
        this.projectRepository = projectRepository;
    }

    public List<Tasks> getAllTasks() {
        return tasksRepository.findAll();
    }

    @Transactional
    public Tasks createTask(Tasks task) {
        if (task.getProject() != null && task.getProject().getId() != null) {
            Long projId = task.getProject().getId();
            Project managed = projectRepository.findById(projId)
                    .orElseThrow(() -> new RuntimeException("Project not found: " + projId));
            task.setProject(managed);
        }
        return tasksRepository.save(task);
    }

    public Tasks updateTasks(Long id, Tasks tasksDetails) {
        Tasks tasks = tasksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        tasks.setTitle(tasksDetails.getTitle());
        tasks.setDescription(tasksDetails.getDescription());
        tasks.setPriority(tasksDetails.getPriority());
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