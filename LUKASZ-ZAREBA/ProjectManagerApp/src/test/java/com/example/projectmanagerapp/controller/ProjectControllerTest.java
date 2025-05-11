package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @Test
    void getAllProjects_returnsListOfProjects() {
        // given
        Project project = new Project();
        project.setId(1);
        project.setName("Test Project");
        List<Project> projects = Collections.singletonList(project);
        given(projectService.getAll()).willReturn(projects);

        // when
        List<Project> result = projectController.getAllProjects();

        // then
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Test Project", result.get(0).getName());
        verify(projectService).getAll();
    }

    @Test
    void getProjectById_returnsProject() {
        // given
        Project project = new Project();
        project.setId(1);
        project.setName("Test Project");
        given(projectService.getById(1)).willReturn(project);

        // when
        Project result = projectController.getProjectById(1);

        // then
        assertEquals(1, result.getId());
        assertEquals("Test Project", result.getName());
        verify(projectService).getById(1);
    }

    @Test
    void createProject_returnsCreatedProject() {
        // given
        Project inputProject = new Project();
        inputProject.setName("New Project");

        Project savedProject = new Project();
        savedProject.setId(1);
        savedProject.setName("New Project");

        given(projectService.create(any(Project.class))).willReturn(savedProject);

        // when
        Project result = projectController.createProject(inputProject);

        // then
        assertEquals(1, result.getId());
        assertEquals("New Project", result.getName());
        verify(projectService).create(inputProject);
    }

    @Test
    void updateProject_returnsUpdatedProject() {
        // given
        Project inputProject = new Project();
        inputProject.setName("Updated Project");

        Project updatedProject = new Project();
        updatedProject.setId(1);
        updatedProject.setName("Updated Project");

        given(projectService.update(eq(1), any(Project.class))).willReturn(updatedProject);


        Project result = projectController.updateProject(1, inputProject);


        assertEquals(1, result.getId());
        assertEquals("Updated Project", result.getName());
        verify(projectService).update(1, inputProject);
    }

    @Test
    void deleteProject_returnsNoContent() {

        doNothing().when(projectService).delete(1);


        ResponseEntity<Void> result = projectController.deleteProject(1);


        assertEquals(204, result.getStatusCodeValue());
        assertNull(result.getBody());
        verify(projectService).delete(1);
    }
}