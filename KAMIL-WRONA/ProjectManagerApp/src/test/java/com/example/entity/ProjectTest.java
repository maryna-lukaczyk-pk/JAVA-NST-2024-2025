package com.example.entity;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {

    @Test
    void testProjectSettersAndGetters() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Project");
        project.setDescription("Desc");

        User user = new User();
        user.setUsername("Tester");

        project.setUsers(List.of(user));

        assertEquals(1L, project.getId());
        assertEquals("Project", project.getName());
        assertEquals("Desc", project.getDescription());
        assertEquals("Tester", project.getUsers().get(0).getUsername());
    }
}
