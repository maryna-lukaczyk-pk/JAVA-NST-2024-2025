package com.example.integration;

import com.example.ProjectManagerAppApplication;
import com.example.entity.Project;
import com.example.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
public class ProjectRepositoryIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void shouldSaveAndFetchProject() {
        Project project = new Project();
        project.setName("integration_project");
        project.setDescription("project description");
        Project saved = projectRepository.save(project);

        Optional<Project> result = projectRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("integration_project", result.get().getName());
    }
}
