package com.example.projectmanagerapp.unit.dto.project;

import org.example.projectmanager.dto.project.ProjectDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProjectDtoTest {
    @Test
    void CorrectlyMapsEntity() {
        var project = new org.example.projectmanager.entity.project.Project();
        project.setId(1L);
        project.setName("Project A");

        var projectDto = ProjectDto.MapFromEntity(project);

        Assertions.assertEquals(project.getId(), projectDto.id);
        Assertions.assertEquals(project.getName(), projectDto.name);
    }
}
