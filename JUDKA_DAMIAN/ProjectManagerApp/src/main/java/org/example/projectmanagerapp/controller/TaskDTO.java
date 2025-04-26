package org.example.projectmanagerapp.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    private String title;
    private String description;
    private String taskType;
    private Integer projectId;
}
