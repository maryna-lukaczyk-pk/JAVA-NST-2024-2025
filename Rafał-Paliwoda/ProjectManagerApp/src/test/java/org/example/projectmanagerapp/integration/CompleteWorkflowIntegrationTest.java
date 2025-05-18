package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
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
public class CompleteWorkflowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should verify complete CRUD workflow across all layers")
    @Transactional
    void shouldVerifyCompleteCrudWorkflowAcrossAllLayers() throws Exception {
        User user = new User();
        user.setUsername("workflowuser");

        MvcResult userResult = mockMvc.perform(post("/api/v1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn();

        User createdUser = objectMapper.readValue(
                userResult.getResponse().getContentAsString(), User.class);

        assertThat(userRepository.findById(createdUser.getId())).isPresent();

        Project project = new Project();
        project.setName("Workflow Project");

        MvcResult projectResult = mockMvc.perform(post("/api/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        assertThat(projectRepository.findById(createdProject.getId())).isPresent();

        mockMvc.perform(post("/api/v1/project/{projectId}/user", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdUser.getId())))
                .andExpect(status().isCreated());

        Project projectWithUser = projectRepository.findById(createdProject.getId()).orElse(null);
        assert projectWithUser != null;
        assertThat(projectWithUser.getUsers()).hasSize(1);
        assertThat(projectWithUser.getUsers().getFirst().getId()).isEqualTo(createdUser.getId());

        Task task = new Task();
        task.setTitle("Workflow Task");
        task.setDescription("Task for workflow testing");
        task.setTaskType("FEATURE");
        task.setProject(createdProject);

        MvcResult taskResult = mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andReturn();

        Task createdTask = objectMapper.readValue(
                taskResult.getResponse().getContentAsString(), Task.class);

        assertThat(taskRepository.findById(createdTask.getId())).isPresent();
        Task dbTask = taskRepository.findById(createdTask.getId()).orElse(null);
        assert dbTask != null;
        assertThat(dbTask.getProject().getId()).isEqualTo(createdProject.getId());

        MvcResult tasksResult = mockMvc.perform(get("/api/v1/project/{projectId}/tasks", createdProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Task[] projectTasks = objectMapper.readValue(
                tasksResult.getResponse().getContentAsString(), Task[].class);

        assertThat(projectTasks).hasSize(1);
        assertThat(projectTasks[0].getId()).isEqualTo(createdTask.getId());

        Task taskUpdate = new Task();
        taskUpdate.setTitle("Updated Workflow Task");
        taskUpdate.setDescription("Updated description");
        taskUpdate.setTaskType("BUG");

        mockMvc.perform(put("/api/v1/task/{id}", createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskUpdate)))
                .andExpect(status().isOk());

        Task updatedDbTask = taskRepository.findById(createdTask.getId()).orElse(null);
        assert updatedDbTask != null;
        assertThat(updatedDbTask.getTitle()).isEqualTo("Updated Workflow Task");
        assertThat(updatedDbTask.getTaskType()).isEqualTo("BUG");

        User userUpdate = new User();
        userUpdate.setUsername("updatedworkflowuser");

        mockMvc.perform(put("/api/v1/user/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk());

        Project finalProject = projectRepository.findById(createdProject.getId()).orElse(null);
        assert finalProject != null;
        assertThat(finalProject.getUsers()).hasSize(1);
        assertThat(finalProject.getUsers().getFirst().getUsername()).isEqualTo("updatedworkflowuser");

        Project projectUpdate = new Project();
        projectUpdate.setName("Updated Workflow Project");

        mockMvc.perform(put("/api/v1/project/{id}", createdProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectUpdate)))
                .andExpect(status().isOk());

        Project updatedProject = projectRepository.findById(createdProject.getId()).orElse(null);
        assert updatedProject != null;
        assertThat(updatedProject.getName()).isEqualTo("Updated Workflow Project");
        assertThat(updatedProject.getUsers()).hasSize(1);

        Task finalTask = taskRepository.findById(createdTask.getId()).orElse(null);
        assert finalTask != null;
        assertThat(finalTask.getProject().getId()).isEqualTo(createdProject.getId());

        mockMvc.perform(delete("/api/v1/task/{id}", createdTask.getId()))
                .andExpect(status().isNoContent());

        assertThat(taskRepository.findById(createdTask.getId())).isEmpty();

        mockMvc.perform(delete("/api/v1/user/{userId}/project/{projectId}",
                        createdUser.getId(), createdProject.getId()))
                .andExpect(status().isNoContent());

        Project projectAfterUserRemoval = projectRepository.findById(createdProject.getId()).orElse(null);
        assert projectAfterUserRemoval != null;
        assertThat(projectAfterUserRemoval.getUsers()).isEmpty();

        mockMvc.perform(delete("/api/v1/project/{id}", createdProject.getId()))
                .andExpect(status().isNoContent());

        assertThat(projectRepository.findById(createdProject.getId())).isEmpty();

        mockMvc.perform(delete("/api/v1/user/{id}", createdUser.getId()))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(createdUser.getId())).isEmpty();

        assertThat(projectRepository.findAll()).isEmpty();
        assertThat(userRepository.findAll()).isEmpty();
        assertThat(taskRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Should verify database constraints and relationships")
    @Transactional
    void shouldVerifyDatabaseConstraintsAndRelationships() throws Exception {
        Project project = new Project();
        project.setName("Multi-relationship Project");
        Project savedProject = projectRepository.save(project);

        User user1 = new User();
        user1.setUsername("user1");
        User savedUser1 = userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        User savedUser2 = userRepository.save(user2);

        mockMvc.perform(post("/api/v1/project/{projectId}/user", savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedUser1.getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/project/{projectId}/user", savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedUser2.getId())))
                .andExpect(status().isCreated());

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("First task");
        task1.setTaskType("FEATURE");
        task1.setProject(savedProject);

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated());

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Second task");
        task2.setTaskType("BUG");
        task2.setProject(savedProject);

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task2)))
                .andExpect(status().isCreated());

        Project projectWithRelations = projectRepository.findById(savedProject.getId()).orElse(null);
        assert projectWithRelations != null;
        assertThat(projectWithRelations.getUsers()).hasSize(2);
        assertThat(projectWithRelations.getUsers())
                .extracting(User::getUsername)
                .containsExactlyInAnyOrder("user1", "user2");

        MvcResult tasksResult = mockMvc.perform(get("/api/v1/project/{projectId}/tasks", savedProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Task[] tasks = objectMapper.readValue(
                tasksResult.getResponse().getContentAsString(), Task[].class);

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2");

        for (Task task : tasks) {
            assertThat(task.getProject()).isNotNull();
            assertThat(task.getProject().getId()).isEqualTo(savedProject.getId());
        }

        mockMvc.perform(delete("/api/v1/project/{id}", savedProject.getId()))
                .andExpect(status().isNoContent());

        assertThat(projectRepository.findById(savedProject.getId())).isEmpty();

        assertThat(taskRepository.findByProjectId(savedProject.getId())).isEmpty();

        assertThat(userRepository.findById(savedUser1.getId())).isPresent();
        assertThat(userRepository.findById(savedUser2.getId())).isPresent();
    }

    @Test
    @DisplayName("Should handle error scenarios across all layers")
    @Transactional
    void shouldHandleErrorScenariosAcrossAllLayers() throws Exception {
        mockMvc.perform(get("/api/v1/project/999"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/v1/user/999"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/v1/task/999"))
                .andExpect(status().isNotFound());

        Project project = new Project();
        project.setName("Non-existent Project");

        mockMvc.perform(put("/api/v1/project/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isNotFound());

        Project realProject = new Project();
        realProject.setName("Real Project");
        Project savedProject = projectRepository.save(realProject);

        mockMvc.perform(post("/api/v1/project/{projectId}/user", savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(999L)))
                .andExpect(status().isInternalServerError());

        Project nonExistentProject = new Project();
        nonExistentProject.setId(999L);

        Task task = new Task();
        task.setTitle("Task for Non-existent Project");
        task.setDescription("This should fail");
        task.setTaskType("FEATURE");
        task.setProject(nonExistentProject);

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/v1/project/999/tasks"))
                .andExpect(status().isNotFound());
    }
}