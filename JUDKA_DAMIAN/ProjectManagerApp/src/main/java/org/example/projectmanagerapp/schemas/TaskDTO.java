package org.example.projectmanagerapp.schemas;

import lombok.Data;
import org.example.projectmanagerapp.entity.TaskType;

@Data
public class TaskDTO {
    private String title;
    private String description;
    private TaskType taskType;
    private Integer projectId;
}
