package org.example.projectmanager.dto.project;

import org.example.projectmanager.entity.project.Project;

public class ProjectCreateDto {
    public String name;

    public Project MapToEntity() {
        var project = new Project();
        project.setName(name);
        return project;
    }
}
