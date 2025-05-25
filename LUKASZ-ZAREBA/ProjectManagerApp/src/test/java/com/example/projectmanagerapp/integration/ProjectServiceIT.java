package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProjectServiceIT extends BaseIT {

    @Autowired
    private ProjectService projectService;

    @Test
    public void testGetAllProjects() {
        Project project1 = new Project();
        project1.setName("Project 1");
        projectService.create(project1);

        Project project2 = new Project();
        project2.setName("Project 2");
        projectService.create(project2);

        List<Project> projects = projectService.getAll();

        assertNotNull(projects);
        assertEquals(2, projects.size());
        assertTrue(projects.stream().anyMatch(p -> "Project 1".equals(p.getName())));
        assertTrue(projects.stream().anyMatch(p -> "Project 2".equals(p.getName())));
    }

    @Test
    public void testGetProjectById() {
        Project project = new Project();
        project.setName("Test Project");
        Project savedProject = projectService.create(project);

        Project retrievedProject = projectService.getById(savedProject.getId());

        assertNotNull(retrievedProject);
        assertEquals(savedProject.getId(), retrievedProject.getId());
        assertEquals("Test Project", retrievedProject.getName());
    }

    @Test
    public void testGetProjectByIdNotFound() {
        // Wywołanie metody getById dla nieistniejącego ID
        assertThrows(NoSuchElementException.class, () -> {
            projectService.getById(9999);
        });
    }

    @Test
    public void testDeleteProject() {
        Project project = new Project();
        project.setName("Project to Delete");
        Project savedProject = projectService.create(project);

        assertNotNull(projectService.getById(savedProject.getId()));

        projectService.delete(savedProject.getId());

        assertThrows(NoSuchElementException.class, () -> {
            projectService.getById(savedProject.getId());
        });
    }

    @Test
    public void testDeleteProjectNotFound() {
        assertThrows(NoSuchElementException.class, () -> {
            projectService.delete(9999);
        });
    }

    @Test
    public void testUpdateProject() {
        Project project = new Project();
        project.setName("Original Name");
        Project savedProject = projectService.create(project);

        Project updates = new Project();
        updates.setName("Updated Name");

        Project updatedProject = projectService.update(savedProject.getId(), updates);

        assertNotNull(updatedProject);
        assertEquals(savedProject.getId(), updatedProject.getId());
        assertEquals("Updated Name", updatedProject.getName());
    }

    @Test
    public void testUpdateProjectNotFound() {
        Project updates = new Project();
        updates.setName("Updated Name");

        assertThrows(NoSuchElementException.class, () -> {
            projectService.update(9999, updates);
        });
    }
}