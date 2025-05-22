package org.example.projectmanagerapp.tests.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TaskServiceIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void taskShouldBeLinkedToProject() {
        Project project = new Project();
        project.setName("Test Project");
        project = projectRepository.save(project);

        Task task = new Task();
        task.setTitle("Linked Task");
        task.setProject(project);
        task = taskRepository.save(task);

        Task savedTask = taskRepository.findById(task.getId()).orElseThrow();
        assertEquals(project.getId(), savedTask.getProject().getId());
    }
}
