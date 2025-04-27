package org.example.projectmanager.dto.task;

import org.example.projectmanager.entity.prioritylevel.PriorityLevel;
import org.example.projectmanager.entity.task.Task;
import org.example.projectmanager.entity.task.TaskType;

public class TaskDto {
    public String title;
    public String description;
    public TaskType taskType;
    public Long projectId;
    public PriorityLevel priorityLevel;

    public static TaskDto MapFromEntity(Task task) {
        var taskDto = new TaskDto();
        taskDto.title = task.getTitle();
        taskDto.description = task.getDescription();
        taskDto.taskType = task.getTaskType();
        taskDto.projectId = task.getProject().getId();
        taskDto.priorityLevel = task.getPriorityLevel();
        return taskDto;
    }
}
