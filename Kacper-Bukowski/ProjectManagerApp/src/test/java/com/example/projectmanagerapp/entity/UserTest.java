package com.example.projectmanagerapp.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {

    @Test
    @DisplayName("User getters and setters should work correctly")
    void userGettersAndSettersTest() {
        User user = new User();
        user.setUsername("testUser");
        
        assertEquals("testUser", user.getUsername());
        
        Project project = new Project();
        project.setName("testProject");
        Set<Project> projects = new HashSet<>();
        projects.add(project);
        
        user.setProjects(projects);
        assertNotNull(user.getProjects());
        assertEquals(1, user.getProjects().size());
        assertEquals("testProject", user.getProjects().iterator().next().getName());
    }
}