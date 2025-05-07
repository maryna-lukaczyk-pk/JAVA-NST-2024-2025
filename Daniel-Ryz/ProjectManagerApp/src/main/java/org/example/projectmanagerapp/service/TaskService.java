package org.example.projectmanagerapp.service;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.service.priority.PriorityAssigner;
import org.example.projectmanagerapp.service.priority.PriorityLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    public final TaskRepository taskRepository;
    private final PriorityAssigner priorityAssigner;

    public ResponseEntity<Tasks> getTasksById(Long id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public Tasks createTask(Tasks tasks) {
        // przypisanie dynamicznego priorytetu
        PriorityLevel level = priorityAssigner.assignPriority(tasks.getTitle());
        tasks.setDynamicPriority(level.getPriority());
        // zapis do bazy
        return taskRepository.save(tasks);
    }
}
