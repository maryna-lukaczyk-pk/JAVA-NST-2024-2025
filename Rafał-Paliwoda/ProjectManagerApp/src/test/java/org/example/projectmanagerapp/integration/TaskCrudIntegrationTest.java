package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class TaskCrudIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private Project testProject;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();

        // Create a test project for tasks
        testProject = new Project();
        testProject.setName("Test Project for Tasks");
        testProject = projectRepository.save(testProject);
    }

    @Test
    @Transactional
    void shouldCreateTask() throws Exception {
        // Given
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("Task Description");
        task.setTaskType("FEATURE");
        task.setProject(testProject);

        // When
        MvcResult result = mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        Task createdTask = objectMapper.readValue(
                result.getResponse().getContentAsString(), Task.class);

        assertThat(createdTask.getId()).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo("New Task");
        assertThat(createdTask.getDescription()).isEqualTo("Task Description");
        assertThat(createdTask.getTaskType()).isEqualTo("FEATURE");
        assertThat(createdTask.getProject().getId()).isEqualTo(testProject.getId());

        // Verify in database
        assertThat(taskRepository.findById(createdTask.getId())).isPresent();
    }

    @Test
    @Transactional
    void shouldRetrieveTaskById() throws Exception {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setTaskType("BUG");
        task.setProject(testProject);
        Task savedTask = taskRepository.save(task);

        // When
        MvcResult result = mockMvc.perform(get("/api/v1/task/{id}", savedTask.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        Task retrievedTask = objectMapper.readValue(
                result.getResponse().getContentAsString(), Task.class);

        assertThat(retrievedTask.getId()).isEqualTo(savedTask.getId());
        assertThat(retrievedTask.getTitle()).isEqualTo("Test Task");
        assertThat(retrievedTask.getDescription()).isEqualTo("Test Description");
        assertThat(retrievedTask.getTaskType()).isEqualTo("BUG");
        assertThat(retrievedTask.getProject().getId()).isEqualTo(testProject.getId());
    }

    @Test
    void shouldReturnNotFoundForNonExistentTask() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/v1/task/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldRetrieveAllTasks() throws Exception {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setTaskType("FEATURE");
        task1.setProject(testProject);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setTaskType("BUG");
        task2.setProject(testProject);
        taskRepository.save(task2);

        // When
        MvcResult result = mockMvc.perform(get("/api/v1/task"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        Task[] tasks = objectMapper.readValue(
                result.getResponse().getContentAsString(), Task[].class);

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2");
        assertThat(tasks).extracting(Task::getTaskType)
                .containsExactlyInAnyOrder("FEATURE", "BUG");
    }

    @Test
    @Transactional
    void shouldCreateTaskWithDifferentTypes() throws Exception {
        // Test creating tasks with different types
        String[] taskTypes = {"FEATURE", "BUG", "ENHANCEMENT", "DOCUMENTATION"};

        for (String taskType : taskTypes) {
            Task task = new Task();
            task.setTitle("Task " + taskType);
            task.setDescription("Description for " + taskType);
            task.setTaskType(taskType);
            task.setProject(testProject);

            MvcResult result = mockMvc.perform(post("/api/v1/task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(task)))
                    .andExpect(status().isCreated())
                    .andReturn();

            Task createdTask = objectMapper.readValue(
                    result.getResponse().getContentAsString(), Task.class);

            assertThat(createdTask.getTaskType()).isEqualTo(taskType);
            assertThat(createdTask.getTitle()).isEqualTo("Task " + taskType);
        }

        // Verify all tasks were created
        assertThat(taskRepository.findAll()).hasSize(taskTypes.length);
    }

    @Test
    void shouldReturnNotFoundWhenCreatingTaskWithNonExistentProject() throws Exception {
        // Given
        Project nonExistentProject = new Project();
        nonExistentProject.setId(999L);

        Task task = new Task();
        task.setTitle("Task for Non-existent Project");
        task.setDescription("This should fail");
        task.setTaskType("FEATURE");
        task.setProject(nonExistentProject);

        // When/Then
        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldReturnEmptyListWhenNoTasks() throws Exception {
        // When
        MvcResult result = mockMvc.perform(get("/api/v1/task"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        Task[] tasks = objectMapper.readValue(
                result.getResponse().getContentAsString(), Task[].class);

        assertThat(tasks).isEmpty();
    }

    @Test
    @Transactional
    void shouldCreateTasksForMultipleProjects() throws Exception {
        // Given - Create another project
        Project project2 = new Project();
        project2.setName("Second Project");
        project2 = projectRepository.save(project2);

        // When - Create tasks for both projects
        Task task1 = new Task();
        task1.setTitle("Task for Project 1");
        task1.setDescription("Description 1");
        task1.setTaskType("FEATURE");
        task1.setProject(testProject);

        Task task2 = new Task();
        task2.setTitle("Task for Project 2");
        task2.setDescription("Description 2");
        task2.setTaskType("BUG");
        task2.setProject(project2);

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task2)))
                .andExpect(status().isCreated());

        // Then - Verify both tasks exist
        MvcResult result = mockMvc.perform(get("/api/v1/task"))
                .andExpect(status().isOk())
                .andReturn();

        Task[] tasks = objectMapper.readValue(
                result.getResponse().getContentAsString(), Task[].class);

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task for Project 1", "Task for Project 2");

        // Verify tasks belong to correct projects
        for (Task task : tasks) {
            if (task.getTitle().equals("Task for Project 1")) {
                assertThat(task.getProject().getId()).isEqualTo(testProject.getId());
            } else {
                assertThat(task.getProject().getId()).isEqualTo(project2.getId());
            }
        }
    }

    @Test
    @Transactional
    void shouldMaintainTaskProjectRelationship() throws Exception {
        // Given - Create task
        Task task = new Task();
        task.setTitle("Relationship Test Task");
        task.setDescription("Testing project relationship");
        task.setTaskType("TEST");
        task.setProject(testProject);

        MvcResult createResult = mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andReturn();

        Task createdTask = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), Task.class);

        // When - Retrieve task by ID
        MvcResult getResult = mockMvc.perform(get("/api/v1/task/{id}", createdTask.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Task retrievedTask = objectMapper.readValue(
                getResult.getResponse().getContentAsString(), Task.class);

        // Then - Verify project relationship is maintained
        assertThat(retrievedTask.getProject()).isNotNull();
        assertThat(retrievedTask.getProject().getId()).isEqualTo(testProject.getId());
        assertThat(retrievedTask.getProject().getName()).isEqualTo(testProject.getName());
    }
}