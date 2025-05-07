package org.example.projectmanagerapp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.CreateTaskRequest;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.service.priority.PriorityAssigner;
import org.example.projectmanagerapp.service.priority.PriorityLevel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    public final TaskRepository taskRepository;
    private final PriorityAssigner priorityAssigner;
    private final ProjectService projectService;

    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    public Tasks getTasksById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Transactional
    public Tasks createTask(CreateTaskRequest request) {
        // przypisanie dynamicznego priorytetu
        PriorityLevel level = priorityAssigner.assignPriority(request.title());

        Tasks task = new Tasks();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setTaskType(request.taskType());
        task.setDynamicPriority(level.getPriority());
        // zapis do bazy
        return taskRepository.save(task);
    }

    @Transactional
    public Tasks updateTask(Long id, CreateTaskRequest request) {
        Tasks task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        PriorityLevel level = priorityAssigner.assignPriority(request.title());
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setTaskType(request.taskType());
        task.setDynamicPriority(level.getPriority());
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void assignTaskToProject(Long taskId, Long projectId) {
        Tasks task = getTasksById(taskId);
        Project project = getProjectById(projectId);

        task.setProject(project);
        taskRepository.save(task);
    }

    private Project getProjectById(Long projectId) {
        return projectService.getProjectById(projectId);
    }
}
