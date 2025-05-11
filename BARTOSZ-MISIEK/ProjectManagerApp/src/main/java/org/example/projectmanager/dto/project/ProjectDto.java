package org.example.projectmanager.dto.project;

import org.example.projectmanager.entity.project.Project;

import java.util.Objects;

public class ProjectDto {
    public Long id;
    public String name;

    public static ProjectDto MapFromEntity(Project project) {
        var self = new ProjectDto();
        self.id = project.getId();
        self.name = project.getName();
        return self;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProjectDto that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
