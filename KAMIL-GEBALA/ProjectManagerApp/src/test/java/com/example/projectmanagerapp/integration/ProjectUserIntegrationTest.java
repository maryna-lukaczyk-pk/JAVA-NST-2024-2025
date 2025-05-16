package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.config.TestConfig;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.ProjectUser;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.ProjectUserRepository;
import com.example.projectmanagerapp.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
public class ProjectUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Test
    public void testAssignUserToProject() throws Exception {
        Users user = new Users();
        user.setUsername("testUser");

        String userJson = objectMapper.writeValueAsString(user);
        MvcResult userResult = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn();

        Users createdUser = objectMapper.readValue(
                userResult.getResponse().getContentAsString(), Users.class);

        Project project = new Project();
        project.setName("testProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult projectResult = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        mockMvc.perform(post("/api/projects/" + createdProject.getId() + "/users/" + createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + createdProject.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(createdUser.getId().intValue())))
                .andExpect(jsonPath("$[0].username", is("testUser")));
    }

    @Test
    public void testProjectUserIdEquals() {
        ProjectUser.ProjectUserId id1 = new ProjectUser.ProjectUserId(1L, 2L);
        ProjectUser.ProjectUserId id2 = new ProjectUser.ProjectUserId(1L, 2L);

        assertTrue(id1.equals(id1));
        assertTrue(id1.equals(id2));
        assertTrue(id2.equals(id1));

        ProjectUser.ProjectUserId id3 = new ProjectUser.ProjectUserId(1L, 3L);
        ProjectUser.ProjectUserId id4 = new ProjectUser.ProjectUserId(3L, 2L);
        assertFalse(id1.equals(id3));
        assertFalse(id1.equals(id4));
        assertFalse(id1.equals(null));
        assertFalse(id1.equals("not a ProjectUserId"));
    }
    
    @Test
    public void testProjectUserIdHashCode() {
        ProjectUser.ProjectUserId id1 = new ProjectUser.ProjectUserId(1L, 2L);
        ProjectUser.ProjectUserId id2 = new ProjectUser.ProjectUserId(1L, 2L);

        assertEquals(id1.hashCode(), id2.hashCode());

        ProjectUser.ProjectUserId id3 = new ProjectUser.ProjectUserId(1L, 3L);
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }
    
    @Test
    public void testProjectUserIdGettersAndSetters() {
        ProjectUser.ProjectUserId id = new ProjectUser.ProjectUserId(1L, 2L);
        assertEquals(1L, id.getProject());
        assertEquals(2L, id.getUser());

        id.setProject(3L);
        id.setUser(4L);
        assertEquals(3L, id.getProject());
        assertEquals(4L, id.getUser());

        ProjectUser.ProjectUserId emptyId = new ProjectUser.ProjectUserId();
        assertNull(emptyId.getProject());
        assertNull(emptyId.getUser());
    }
    
    @Test
    public void testProjectUserGettersAndSetters() {
        Project project = new Project();
        project.setName("Test Project");
        project = projectRepository.save(project);
        
        Users user = new Users();
        user.setUsername("Test User");
        user = usersRepository.save(user);

        ProjectUser projectUser = new ProjectUser();
        projectUser.setProject(project);
        projectUser.setUser(user);
        
        assertEquals(project, projectUser.getProject());
        assertEquals(user, projectUser.getUser());

        projectUserRepository.save(projectUser);
        
        ProjectUser.ProjectUserId id = new ProjectUser.ProjectUserId();
        id.setProject(project.getId());
        id.setUser(user.getId());
        
        assertTrue(projectUserRepository.existsById(id));
    }
}