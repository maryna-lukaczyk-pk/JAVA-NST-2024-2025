package com.example.integration;

import com.example.ProjectManagerAppApplication;
import com.example.entity.Project;
import com.example.entity.Task;
import com.example.entity.User;
import com.example.repository.ProjectRepository;
import com.example.repository.TaskRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
public class TaskRepositoryIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        // Usunięcie testowych danych przed każdym testem
        User user = userRepository.findByUsername("integration_user");
        if (user != null) {
            taskRepository.deleteAll(taskRepository.findAll()); // usunięcie powiązanych zadań
            userRepository.delete(user);
        }

        Project project = projectRepository.findAll().stream()
                .filter(p -> "integration_project".equals(p.getName()))
                .findFirst().orElse(null);
        if (project != null) {
            projectRepository.delete(project);
        }

        userRepository.flush();
        projectRepository.flush();
        taskRepository.flush();
    }

    @Test
    void shouldSaveAndFetchTaskWithRelations() {
        // Tworzymy użytkownika
        User user = new User();
        user.setUsername("integration_user");
        User savedUser = userRepository.save(user);

        // Tworzymy projekt
        Project project = new Project();
        project.setName("integration_project");
        project.setDescription("desc");
        Project savedProject = projectRepository.save(project);

        // Tworzymy zadanie
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setUser(savedUser);
        task.setProject(savedProject);
        task.setTaskType("TASK");

        taskRepository.save(task);

        // Pobieramy z bazy i sprawdzamy dane
        List<Task> tasks = taskRepository.findAll();
        assertFalse(tasks.isEmpty());

        Task fetchedTask = tasks.get(0);
        assertEquals("Test Task", fetchedTask.getTitle());
        assertEquals("integration_user", fetchedTask.getUser().getUsername());
        assertEquals("integration_project", fetchedTask.getProject().getName());
        assertEquals("TASK", fetchedTask.getTaskType());
    }
}
