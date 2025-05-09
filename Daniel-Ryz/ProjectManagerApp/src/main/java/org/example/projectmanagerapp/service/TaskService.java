package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    public final TaskRepository taskRepository;
    private final PriorityAssigner priorityAssigner;

    @Autowired
    public TaskService(TaskRepository taskRepository, PriorityAssigner priorityAssigner) {
        this.taskRepository = taskRepository;
        this.priorityAssigner = priorityAssigner;
    }

    public Tasks createTask(Tasks tasks) {
        // przypisanie dynamicznego priorytetu
        PriorityLevel level = priorityAssigner.assignPriority(tasks.getTitle());
        tasks.setDynamicPriority(level.getPriority());

        // zapis do bazy
        return taskRepository.save(tasks);
    }
}
