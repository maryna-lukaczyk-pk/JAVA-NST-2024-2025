package org.example.projectmanager.dto.project;

import org.example.projectmanager.entity.project.Project;

public class ProjectEditDto {
    public Long id;
    public String name;

    public void UpdateEntity(Project project) {
        project.setId(id);
        project.setName(name);
    }
}
