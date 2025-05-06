package org.example.projectmanagerapp.schemes;

import lombok.Getter;
import lombok.Setter;
import org.example.projectmanagerapp.entity.TaskType;

@Getter
@Setter
public class TaskDTO {
    private String title;
    private String description;
    private TaskType taskType;
    private Integer projectId;
}
