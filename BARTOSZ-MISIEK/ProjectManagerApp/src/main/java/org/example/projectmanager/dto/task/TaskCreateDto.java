package org.example.projectmanager.dto.task;

import org.example.projectmanager.entity.prioritylevel.PriorityLevel;
import org.example.projectmanager.entity.project.Project;
import org.example.projectmanager.entity.task.Task;
import org.example.projectmanager.entity.task.TaskType;

public class TaskCreateDto {
    public String title;
    public String description;
    public TaskType taskType;
    public Project project;
    public PriorityLevel priorityLevel;

    public Task MapToEntity() {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setTaskType(taskType);
        task.setProject(project);
        task.setPriorityLevel(priorityLevel);
        return task;
    }
}
