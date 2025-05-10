package com.example.projectmanagerapp.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProjectTest {

    @Test
    @DisplayName("Project getters and setters should work correctly")
    void projectGettersAndSettersTest() {
        Project project = new Project();
        project.setName("testProject");
        
        assertEquals("testProject", project.getName());
        
        User user = new User();
        user.setUsername("testUser");
        Set<User> users = new HashSet<>();
        users.add(user);
        
        project.setUsers(users);
        assertNotNull(project.getUsers());
        assertEquals(1, project.getUsers().size());
        assertEquals("testUser", project.getUsers().iterator().next().getUsername());
        
        Task task = new Task();
        task.setTitle("testTask");
        task.setDescription("testDescription");
        task.setTaskTypeEnum(TaskType.BUG);
        task.setProject(project);
        
        assertNotNull(project.getTasks());
    }
}