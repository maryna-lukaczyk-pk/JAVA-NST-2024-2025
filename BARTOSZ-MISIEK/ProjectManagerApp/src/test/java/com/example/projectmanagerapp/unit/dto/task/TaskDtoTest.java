package com.example.projectmanagerapp.unit.dto.task;

import org.example.projectmanager.dto.task.TaskDto;
import org.example.projectmanager.dto.user.UserDto;
import org.example.projectmanager.entity.prioritylevel.HighPriority;
import org.example.projectmanager.entity.prioritylevel.PriorityLevel;
import org.example.projectmanager.entity.project.Project;
import org.example.projectmanager.entity.task.Task;
import org.example.projectmanager.entity.task.TaskType;
import org.example.projectmanager.entity.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaskDtoTest {
    @Test
    void CorrectlyMapsEntity() {
        var task = new Task();
        task.setId(1L);
        task.setTitle("Task A");
        task.setDescription("Description A");
        task.setTaskType(TaskType.HOUSEWORK);
        task.setPriorityLevel(new HighPriority());

        var taskDto = TaskDto.MapFromEntity(task);
        Assertions.assertEquals(task.getId(), taskDto.id);
        Assertions.assertEquals(task.getTitle(), taskDto.title);
        Assertions.assertEquals(task.getDescription(), taskDto.description);
        Assertions.assertEquals(task.getTaskType(), taskDto.taskType);
        Assertions.assertEquals(task.getPriorityLevel().getClass(), taskDto.priorityLevel.getClass());
        Assertions.assertEquals(task.getProject().map(Project::getId), taskDto.projectId);
    }
}
