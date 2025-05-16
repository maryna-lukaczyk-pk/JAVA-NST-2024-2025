package com.example.projectmanagerapp.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectUserEntityTest {

    @Test
    public void testProjectUserIdEquals() {
        // Create two identical ProjectUserId objects
        ProjectUser.ProjectUserId id1 = new ProjectUser.ProjectUserId(1L, 2L);
        ProjectUser.ProjectUserId id2 = new ProjectUser.ProjectUserId(1L, 2L);
        
        // Test equals method
        assertTrue(id1.equals(id1)); // Same object
        assertTrue(id1.equals(id2)); // Equal objects
        assertTrue(id2.equals(id1)); // Symmetric
        
        // Test not equals
        ProjectUser.ProjectUserId id3 = new ProjectUser.ProjectUserId(1L, 3L);
        ProjectUser.ProjectUserId id4 = new ProjectUser.ProjectUserId(3L, 2L);
        assertFalse(id1.equals(id3));
        assertFalse(id1.equals(id4));
        assertFalse(id1.equals(null));
        assertFalse(id1.equals("not a ProjectUserId"));
    }
    
    @Test
    public void testProjectUserIdHashCode() {
        // Create two identical ProjectUserId objects
        ProjectUser.ProjectUserId id1 = new ProjectUser.ProjectUserId(1L, 2L);
        ProjectUser.ProjectUserId id2 = new ProjectUser.ProjectUserId(1L, 2L);
        
        // Test hashCode
        assertEquals(id1.hashCode(), id2.hashCode());
        
        // Different objects should have different hashCodes
        ProjectUser.ProjectUserId id3 = new ProjectUser.ProjectUserId(1L, 3L);
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }
    
    @Test
    public void testProjectUserIdGettersAndSetters() {
        // Test constructor and getters
        ProjectUser.ProjectUserId id = new ProjectUser.ProjectUserId(1L, 2L);
        assertEquals(1L, id.getProject());
        assertEquals(2L, id.getUser());
        
        // Test setters
        id.setProject(3L);
        id.setUser(4L);
        assertEquals(3L, id.getProject());
        assertEquals(4L, id.getUser());
        
        // Test default constructor
        ProjectUser.ProjectUserId emptyId = new ProjectUser.ProjectUserId();
        assertNull(emptyId.getProject());
        assertNull(emptyId.getUser());
    }
    
    @Test
    public void testProjectUserGettersAndSetters() {
        // Create test objects
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        
        Users user = new Users();
        user.setId(2L);
        user.setUsername("Test User");
        
        // Test ProjectUser
        ProjectUser projectUser = new ProjectUser();
        projectUser.setProject(project);
        projectUser.setUser(user);
        
        assertEquals(project, projectUser.getProject());
        assertEquals(user, projectUser.getUser());
    }
}