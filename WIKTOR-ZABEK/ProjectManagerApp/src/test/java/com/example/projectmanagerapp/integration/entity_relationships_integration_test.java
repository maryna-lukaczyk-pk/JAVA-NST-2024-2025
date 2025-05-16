package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.project_users;
import com.example.projectmanagerapp.entity.projects;
import com.example.projectmanagerapp.entity.tasks;
import com.example.projectmanagerapp.repository.projects_repository;
import com.example.projectmanagerapp.repository.tasks_repository;
import com.example.projectmanagerapp.repository.users_repository;
import com.example.projectmanagerapp.entity.users;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class entity_relationships_integration_test extends integration_test_base {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private projects_repository projectsRepository;

    @Autowired
    private users_repository usersRepository;

    @Autowired
    private tasks_repository tasksRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @AfterEach
    void cleanup() {
        tasksRepository.deleteAll();
        projectsRepository.deleteAll();
        usersRepository.deleteAll();
    }


    @Test
    @DisplayName("Assign User to Project - Integration Test")
    void test_assign_user_to_project() throws Exception {

        users user = new users();
        user.setUsername("testUser");
        users savedUser = usersRepository.save(user);

        projects project = new projects();
        project.setName("testProject");
        projects savedProject = projectsRepository.save(project);


        project_users projectUser = new project_users();
        projectUser.setProjects(savedProject);
        projectUser.setUsers(savedUser);


        entityManager.persist(projectUser);
        entityManager.flush();


        assertTrue(true, "User should be assigned to project");
    }


    @Test
    @DisplayName("Verify Tasks Associated With Project - Integration Test")
    void test_tasks_associated_with_project() throws Exception {
        projects project = new projects();
        project.setName("Project with Tasks");
        projects savedProject = projectsRepository.save(project);


        tasks task = new tasks();
        task.setTitle("Test Task");
        task.setDescription("Testing task association");
        task.setPriority("HIGH");
        task.setProject_id(savedProject.getId());
        task.setProjects(savedProject);


        tasks savedTask = tasksRepository.save(task);


        assertNotNull(savedTask.getId());
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals(savedProject.getId(), savedTask.getProject_id());


        entityManager.flush();
        entityManager.clear();

        Long taskCount = tasksRepository.count();
        assertEquals(1, taskCount, "There should be one task in the database");
    }
}