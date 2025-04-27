package org.example.projectmanagerapp.interfaces;

import lombok.Getter;
import lombok.Setter;
import org.example.projectmanagerapp.model.PriorityLevelEnum;

@Getter
@Setter
public class TaskRequestDTO {
    private String title;
    private String description;
    private PriorityLevelEnum taskType;
    private Long projectId;
}