package org.example.projectmanager.dto.task;

import org.example.projectmanager.entity.prioritylevel.PriorityLevel;
import org.example.projectmanager.entity.task.Task;
import org.example.projectmanager.entity.task.TaskType;

public class TaskEditDto {
    public Long id;
    public String title;
    public String description;
    public TaskType taskType;
    public PriorityLevel priorityLevel;

    public void UpdateEntity(Task task) {
        task.setId(this.id);
        task.setTitle(title);
        task.setDescription(description);
        task.setTaskType(taskType);
        task.setPriorityLevel(priorityLevel);
    }
}
