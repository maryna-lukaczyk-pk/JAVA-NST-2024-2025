package org.example.projectmanager.dto.project;

import org.example.projectmanager.entity.project.Project;

public class ProjectDto {
    public String name;

    public static ProjectDto MapFromEntity(Project project) {
        var self = new ProjectDto();
        self.name = project.getName();
        return self;
    }
}
