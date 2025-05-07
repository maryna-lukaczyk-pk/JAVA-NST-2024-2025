package org.example.projectmanagerapp.dto;

import org.example.projectmanagerapp.entity.TaskType;

public record CreateTaskRequest(
        String title,
        String description,
        TaskType taskType
) {

}
