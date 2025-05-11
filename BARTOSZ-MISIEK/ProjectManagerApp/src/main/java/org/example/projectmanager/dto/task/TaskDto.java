package org.example.projectmanager.dto.task;

import org.example.projectmanager.entity.prioritylevel.PriorityLevel;
import org.example.projectmanager.entity.project.Project;
import org.example.projectmanager.entity.task.Task;
import org.example.projectmanager.entity.task.TaskType;

import java.util.Objects;
import java.util.Optional;

public class TaskDto {
    public Long id;
    public String title;
    public String description;
    public TaskType taskType;
    public Optional<Long> projectId;
    public PriorityLevel priorityLevel;

    public static TaskDto MapFromEntity(Task task) {
        var taskDto = new TaskDto();
        taskDto.id = task.getId();
        taskDto.title = task.getTitle();
        taskDto.description = task.getDescription();
        taskDto.taskType = task.getTaskType();
        taskDto.projectId = task.getProject().map(Project::getId);
        taskDto.priorityLevel = task.getPriorityLevel();
        return taskDto;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TaskDto taskDto)) return false;
        return Objects.equals(id, taskDto.id) && Objects.equals(title, taskDto.title) && Objects.equals(description, taskDto.description) && taskType == taskDto.taskType && Objects.equals(projectId, taskDto.projectId) && Objects.equals(priorityLevel, taskDto.priorityLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, taskType, projectId, priorityLevel);
    }
}
