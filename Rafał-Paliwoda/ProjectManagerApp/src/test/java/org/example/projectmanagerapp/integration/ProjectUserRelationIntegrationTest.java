package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProjectUserRelationIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should assign user to project - Lab example")
    @Transactional
    void shouldAssignUserToProject() throws Exception {
        User newUser = new User();
        newUser.setUsername("testuser");

        MvcResult userResult = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andReturn();

        User createdUser = objectMapper.readValue(
                userResult.getResponse().getContentAsString(), User.class);
        assertThat(createdUser.getId()).isNotNull();

        Project newProject = new Project();
        newProject.setName("Test Project");

        MvcResult projectResult = mockMvc.perform(post("/api/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProject)))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);
        assertThat(createdProject.getId()).isNotNull();

        mockMvc.perform(post("/api/v1/project/{projectId}/user", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser.getId())))
                .andExpect(status().isCreated());

        MvcResult verificationResult = mockMvc.perform(get("/api/v1/project/{id}", createdProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Project retrievedProject = objectMapper.readValue(
                verificationResult.getResponse().getContentAsString(), Project.class);

        assertThat(retrievedProject.getUsers()).isNotNull();
        assertThat(retrievedProject.getUsers()).hasSize(1);
        assertThat(retrievedProject.getUsers().get(0).getId()).isEqualTo(createdUser.getId());
        assertThat(retrievedProject.getUsers().get(0).getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should assign multiple users to project")
    @Transactional
    void shouldAssignMultipleUsersToProject() throws Exception {
        Project project = new Project();
        project.setName("Multi-User Project");

        MvcResult projectResult = mockMvc.perform(post("/api/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        User user1 = new User();
        user1.setUsername("user1");

        MvcResult user1Result = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1)))
                .andExpect(status().isCreated())
                .andReturn();

        User createdUser1 = objectMapper.readValue(
                user1Result.getResponse().getContentAsString(), User.class);

        mockMvc.perform(post("/api/v1/project/{projectId}/user", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser1.getId())))
                .andExpect(status().isCreated());

        User user2 = new User();
        user2.setUsername("user2");

        MvcResult user2Result = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user2)))
                .andExpect(status().isCreated())
                .andReturn();

        User createdUser2 = objectMapper.readValue(
                user2Result.getResponse().getContentAsString(), User.class);

        mockMvc.perform(post("/api/v1/project/{projectId}/user", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser2.getId())))
                .andExpect(status().isCreated());

        MvcResult verificationResult = mockMvc.perform(get("/api/v1/project/{id}", createdProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Project retrievedProject = objectMapper.readValue(
                verificationResult.getResponse().getContentAsString(), Project.class);

        assertThat(retrievedProject.getUsers()).hasSize(2);
        assertThat(retrievedProject.getUsers())
                .extracting(User::getUsername)
                .containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    @DisplayName("Should not assign same user twice to same project")
    @Transactional
    void shouldNotAssignSameUserTwiceToSameProject() throws Exception {
        Project project = new Project();
        project.setName("Test Project");
        Project createdProject = projectRepository.save(project);

        User user = new User();
        user.setUsername("testuser");
        User createdUser = userRepository.save(user);

        mockMvc.perform(post("/api/v1/project/{projectId}/user", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser.getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/project/{projectId}/user", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser.getId())))
                .andExpect(status().isCreated());

        MvcResult verificationResult = mockMvc.perform(get("/api/v1/project/{id}", createdProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Project retrievedProject = objectMapper.readValue(
                verificationResult.getResponse().getContentAsString(), Project.class);

        assertThat(retrievedProject.getUsers()).hasSize(1);
        assertThat(retrievedProject.getUsers().get(0).getId()).isEqualTo(createdUser.getId());
    }

    @Test
    @DisplayName("Should handle error when assigning non-existent user to project")
    @Transactional
    void shouldHandleErrorWhenAssigningNonExistentUserToProject() throws Exception {
        Project project = new Project();
        project.setName("Test Project");
        Project createdProject = projectRepository.save(project);

        Long nonExistentUserId = 999L;

        mockMvc.perform(post("/api/v1/project/{projectId}/user", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentUserId)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should handle error when assigning user to non-existent project")
    @Transactional
    void shouldHandleErrorWhenAssigningUserToNonExistentProject() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        User createdUser = userRepository.save(user);

        Long nonExistentProjectId = 999L;

        mockMvc.perform(post("/api/v1/project/{projectId}/user", nonExistentProjectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser.getId())))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should remove user from project using UserController")
    @Transactional
    void shouldRemoveUserFromProject() throws Exception {
        Project project = new Project();
        project.setName("Test Project");
        Project createdProject = projectRepository.save(project);

        User user = new User();
        user.setUsername("testuser");
        User createdUser = userRepository.save(user);

        mockMvc.perform(post("/api/v1/project/{projectId}/user", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser.getId())))
                .andExpect(status().isCreated());

        Project retrievedProject = projectRepository.findById(createdProject.getId()).orElse(null);
        assertThat(retrievedProject.getUsers()).hasSize(1);

        mockMvc.perform(delete("/api/v1/user/{userId}/project/{projectId}",
                        createdUser.getId(), createdProject.getId()))
                .andExpect(status().isNoContent());

        Project finalProject = projectRepository.findById(createdProject.getId()).orElse(null);
        assertThat(finalProject.getUsers()).isEmpty();
    }

    @Test
    @DisplayName("Should verify project-user relationship integrity during CRUD operations")
    @Transactional
    void shouldVerifyProjectUserRelationshipIntegrity() throws Exception {
        Project project = new Project();
        project.setName("Integrity Test Project");
        Project createdProject = projectRepository.save(project);

        User user = new User();
        user.setUsername("integrityuser");
        User createdUser = userRepository.save(user);

        mockMvc.perform(post("/api/v1/project/{projectId}/user", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser.getId())))
                .andExpect(status().isCreated());

        User updatedUserData = new User();
        updatedUserData.setUsername("updateduser");

        mockMvc.perform(put("/api/v1/user/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserData)))
                .andExpect(status().isOk());

        Project updatedProjectData = new Project();
        updatedProjectData.setName("Updated Integrity Test Project");

        mockMvc.perform(put("/api/v1/project/{id}", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProjectData)))
                .andExpect(status().isOk());

        MvcResult finalResult = mockMvc.perform(get("/api/v1/project/{id}", createdProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Project finalProject = objectMapper.readValue(
                finalResult.getResponse().getContentAsString(), Project.class);

        assertThat(finalProject.getName()).isEqualTo("Updated Integrity Test Project");
        assertThat(finalProject.getUsers()).hasSize(1);
        assertThat(finalProject.getUsers().get(0).getUsername()).isEqualTo("updateduser");
    }

    @Test
    @DisplayName("Should get project tasks after adding users")
    @Transactional
    void shouldGetProjectTasksAfterAddingUsers() throws Exception {
        Project project = new Project();
        project.setName("Project with Tasks and Users");
        Project createdProject = projectRepository.save(project);

        User user = new User();
        user.setUsername("taskuser");
        User createdUser = userRepository.save(user);

        mockMvc.perform(post("/api/v1/project/{projectId}/user", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser.getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/project/{projectId}/tasks", createdProject.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        MvcResult projectResult = mockMvc.perform(get("/api/v1/project/{id}", createdProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Project retrievedProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        assertThat(retrievedProject.getUsers()).hasSize(1);
        assertThat(retrievedProject.getUsers().get(0).getUsername()).isEqualTo("taskuser");
    }

    @Test
    @DisplayName("Should test getUserById returning null through API")
    @Transactional
    void shouldTestGetUserByIdReturningNull() throws Exception {
        mockMvc.perform(get("/api/v1/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should test exception handling in updateUser via API")
    @Transactional
    void shouldTestExceptionHandlingInUpdateUser() throws Exception {
        User updateData = new User();
        updateData.setUsername("updateuser");

        mockMvc.perform(put("/api/v1/user/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should test exception handling in deleteUser via API")
    @Transactional
    void shouldTestExceptionHandlingInDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should test successful user operations through complete API flow")
    @Transactional
    void shouldTestSuccessfulUserOperationsThroughCompleteApiFlow() throws Exception {
        User user = new User();
        user.setUsername("completeflowuser");

        MvcResult createResult = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn();

        User createdUser = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), User.class);

        mockMvc.perform(get("/api/v1/user/{id}", createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("completeflowuser"));

        mockMvc.perform(get("/api/v1/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").value("completeflowuser"));

        User updateData = new User();
        updateData.setUsername("updatedcompleteflowuser");

        mockMvc.perform(put("/api/v1/user/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedcompleteflowuser"));

        Project project = new Project();
        project.setName("Complete Flow Project");
        Project createdProject = projectRepository.save(project);

        mockMvc.perform(post("/api/v1/user/{userId}/project", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdProject.getId())))
                .andExpect(status().isCreated());

        Project projectWithUser = projectRepository.findById(createdProject.getId()).orElse(null);
        assertThat(projectWithUser.getUsers()).hasSize(1);

        mockMvc.perform(delete("/api/v1/user/{userId}/project/{projectId}",
                        createdUser.getId(), createdProject.getId()))
                .andExpect(status().isNoContent());

        Project projectWithoutUser = projectRepository.findById(createdProject.getId()).orElse(null);
        assertThat(projectWithoutUser.getUsers()).isEmpty();

        mockMvc.perform(delete("/api/v1/user/{id}", createdUser.getId()))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(createdUser.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should test multiple projects and users relationships")
    @Transactional
    void shouldTestMultipleProjectsAndUsersRelationships() throws Exception {
        User[] users = new User[3];
        for (int i = 0; i < 3; i++) {
            users[i] = new User();
            users[i].setUsername("user" + (i + 1));
            users[i] = userRepository.save(users[i]);
        }

        Project[] projects = new Project[3];
        for (int i = 0; i < 3; i++) {
            projects[i] = new Project();
            projects[i].setName("Project " + (i + 1));
            projects[i].setUsers(new java.util.ArrayList<>());
            projects[i] = projectRepository.save(projects[i]);
        }

        mockMvc.perform(post("/api/v1/user/{userId}/project", users[0].getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projects[0].getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/user/{userId}/project", users[0].getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projects[1].getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/user/{userId}/project", users[1].getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projects[1].getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/user/{userId}/project", users[1].getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projects[2].getId())))
                .andExpect(status().isCreated());

        // User3 -> Project1, Project3
        mockMvc.perform(post("/api/v1/user/{userId}/project", users[2].getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projects[0].getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/user/{userId}/project", users[2].getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projects[2].getId())))
                .andExpect(status().isCreated());

        Project project1 = projectRepository.findById(projects[0].getId()).orElse(null);
        assertThat(project1.getUsers()).hasSize(2);
        assertThat(project1.getUsers()).extracting(User::getUsername)
                .containsExactlyInAnyOrder("user1", "user3");

        Project project2 = projectRepository.findById(projects[1].getId()).orElse(null);
        assertThat(project2.getUsers()).hasSize(2);
        assertThat(project2.getUsers()).extracting(User::getUsername)
                .containsExactlyInAnyOrder("user1", "user2");

        Project project3 = projectRepository.findById(projects[2].getId()).orElse(null);
        assertThat(project3.getUsers()).hasSize(2);
        assertThat(project3.getUsers()).extracting(User::getUsername)
                .containsExactlyInAnyOrder("user2", "user3");

        mockMvc.perform(delete("/api/v1/user/{userId}/project/{projectId}",
                        users[1].getId(), projects[1].getId()))
                .andExpect(status().isNoContent());

        Project updatedProject2 = projectRepository.findById(projects[1].getId()).orElse(null);
        assertThat(updatedProject2.getUsers()).hasSize(1);
        assertThat(updatedProject2.getUsers().get(0).getUsername()).isEqualTo("user1");

        Project stillProject3 = projectRepository.findById(projects[2].getId()).orElse(null);
        assertThat(stillProject3.getUsers()).hasSize(2);
    }
}