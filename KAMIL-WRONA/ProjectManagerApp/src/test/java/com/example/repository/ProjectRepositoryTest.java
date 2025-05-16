package com.example.repository;

import com.example.ProjectManagerAppApplication;
import com.example.entity.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void shouldSaveAndFindProject() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("This is a test project");

        Project savedProject = projectRepository.save(project);
        projectRepository.flush();

        assertNotNull(savedProject.getId());
        assertEquals("Test Project", savedProject.getName());
        assertEquals("This is a test project", savedProject.getDescription());
    }
}
