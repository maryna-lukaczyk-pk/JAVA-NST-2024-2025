package org.example.projectmanagerapp.interfaces;

import lombok.Getter;
import lombok.Setter;
import org.example.projectmanagerapp.model.PriorityLevelEnum;

@Getter
@Setter
public class ProjectTasksResponseDTO {
    private Long id;
    private String title;
    private String description;
    private PriorityLevelEnum taskType;
}
