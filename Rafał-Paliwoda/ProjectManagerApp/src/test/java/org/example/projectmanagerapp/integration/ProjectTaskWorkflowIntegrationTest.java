package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
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
public class ProjectTaskWorkflowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    @DisplayName("Should test complete project-task workflow covering all TaskService branches")
    @Transactional
    void shouldTestCompleteProjectTaskWorkflowCoveringAllBranches() throws Exception {
        Project project = new Project();
        project.setName("Workflow Project");

        MvcResult projectResult = mockMvc.perform(post("/api/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                projectResult.getResponse().getContentAsString(), Project.class);

        Task task1 = new Task();
        task1.setTitle("First Task");
        task1.setDescription("First task description");
        task1.setTaskType("FEATURE");
        task1.setProject(createdProject);

        MvcResult taskResult1 = mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated())
                .andReturn();

        Task createdTask1 = objectMapper.readValue(
                taskResult1.getResponse().getContentAsString(), Task.class);

        assertThat(createdTask1.getProject().getId()).isEqualTo(createdProject.getId());

        Task task2 = new Task();
        task2.setTitle("Second Task");
        task2.setDescription("Task for update test");
        task2.setTaskType("BUG");
        task2.setProject(createdProject);

        MvcResult taskResult2 = mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task2)))
                .andExpect(status().isCreated())
                .andReturn();

        Task createdTask2 = objectMapper.readValue(
                taskResult2.getResponse().getContentAsString(), Task.class);

        MvcResult projectTasksResult = mockMvc.perform(get("/api/v1/project/{projectId}/tasks", createdProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Task[] projectTasks = objectMapper.readValue(
                projectTasksResult.getResponse().getContentAsString(), Task[].class);

        assertThat(projectTasks).hasSize(2);
        assertThat(projectTasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("First Task", "Second Task");

        Task updateData1 = new Task();
        updateData1.setTitle("Updated First Task");
        updateData1.setDescription("Updated description");
        updateData1.setTaskType("ENHANCEMENT");

        mockMvc.perform(put("/api/v1/task/{id}", createdTask1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData1)))
                .andExpect(status().isOk());

        Task updatedTask1 = taskRepository.findById(createdTask1.getId()).orElse(null);
        assertThat(updatedTask1.getProject().getId()).isEqualTo(createdProject.getId());
        assertThat(updatedTask1.getTitle()).isEqualTo("Updated First Task");

        Project project2 = new Project();
        project2.setName("Second Project");

        MvcResult project2Result = mockMvc.perform(post("/api/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project2)))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject2 = objectMapper.readValue(
                project2Result.getResponse().getContentAsString(), Project.class);

        Task moveTaskData = new Task();
        moveTaskData.setTitle("Moved Task");
        moveTaskData.setDescription("Task moved between projects");
        moveTaskData.setTaskType("MOVED");
        moveTaskData.setProject(createdProject2);

        mockMvc.perform(put("/api/v1/task/{id}", createdTask1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(moveTaskData)))
                .andExpect(status().isOk());

        Task movedTask = taskRepository.findById(createdTask1.getId()).orElse(null);
        assertThat(movedTask.getProject().getId()).isEqualTo(createdProject2.getId());

        MvcResult project1TasksAfterMove = mockMvc.perform(get("/api/v1/project/{projectId}/tasks", createdProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Task[] project1RemainingTasks = objectMapper.readValue(
                project1TasksAfterMove.getResponse().getContentAsString(), Task[].class);

        assertThat(project1RemainingTasks).hasSize(1);
        assertThat(project1RemainingTasks[0].getId()).isEqualTo(createdTask2.getId());

        MvcResult project2TasksResult = mockMvc.perform(get("/api/v1/project/{projectId}/tasks", createdProject2.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Task[] project2Tasks = objectMapper.readValue(
                project2TasksResult.getResponse().getContentAsString(), Task[].class);

        assertThat(project2Tasks).hasSize(1);
        assertThat(project2Tasks[0].getTitle()).isEqualTo("Moved Task");
        assertThat(project2Tasks[0].getId()).isEqualTo(createdTask1.getId());

        Task updateWithProjectWithoutId = new Task();
        updateWithProjectWithoutId.setTitle("Updated with Project Without ID");
        updateWithProjectWithoutId.setDescription("Testing null project ID branch");
        updateWithProjectWithoutId.setTaskType("TEST");

        Project projectWithoutIdForUpdate = new Project();
        projectWithoutIdForUpdate.setName("Project Without ID for Update");
        updateWithProjectWithoutId.setProject(projectWithoutIdForUpdate);

        mockMvc.perform(put("/api/v1/task/{id}", createdTask1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateWithProjectWithoutId)))
                .andExpect(status().isOk());

        Task taskAfterUpdate = taskRepository.findById(createdTask1.getId()).orElse(null);
        assertThat(taskAfterUpdate.getProject().getId()).isEqualTo(createdProject2.getId());

        mockMvc.perform(delete("/api/v1/task/{id}", createdTask2.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/project/{projectId}/tasks", createdProject.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        mockMvc.perform(delete("/api/v1/project/{id}", createdProject2.getId()))
                .andExpect(status().isNoContent());

        assertThat(projectRepository.findById(createdProject2.getId())).isEmpty();

        assertThat(taskRepository.findByProjectId(createdProject2.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should test error scenarios in project-task workflow")
    @Transactional
    void shouldTestErrorScenariosInProjectTaskWorkflow() throws Exception {
        Project nonExistentProject = new Project();
        nonExistentProject.setId(999L);

        Task taskForNonExistentProject = new Task();
        taskForNonExistentProject.setTitle("Task for Non-existent Project");
        taskForNonExistentProject.setDescription("This should fail");
        taskForNonExistentProject.setTaskType("ERROR_TEST");
        taskForNonExistentProject.setProject(nonExistentProject);

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskForNonExistentProject)))
                .andExpect(status().isNotFound());

        Project project = new Project();
        project.setName("Error Test Project");
        Project savedProject = projectRepository.save(project);

        Task task = new Task();
        task.setTitle("Error Test Task");
        task.setProject(savedProject);
        Task savedTask = taskRepository.save(task);

        Project nonExistentProject2 = new Project();
        nonExistentProject2.setId(888L);

        Task moveToNonExistentProject = new Task();
        moveToNonExistentProject.setTitle("Move to Non-existent");
        moveToNonExistentProject.setProject(nonExistentProject2);

        mockMvc.perform(put("/api/v1/task/{id}", savedTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(moveToNonExistentProject)))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/v1/project/777/tasks"))
                .andExpect(status().isNotFound());

        Task updateNonExistentTask = new Task();
        updateNonExistentTask.setTitle("Update Non-existent Task");

        mockMvc.perform(put("/api/v1/task/666")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateNonExistentTask)))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/v1/task/555"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should test bulk operations on project tasks")
    @Transactional
    void shouldTestBulkOperationsOnProjectTasks() throws Exception {
        Project project = new Project();
        project.setName("Bulk Operations Project");
        Project savedProject = projectRepository.save(project);

        String[] taskTypes = {"FEATURE", "BUG", "ENHANCEMENT", "DOCUMENTATION", "REFACTOR"};
        Task[] createdTasks = new Task[taskTypes.length];

        for (int i = 0; i < taskTypes.length; i++) {
            Task task = new Task();
            task.setTitle("Bulk Task " + (i + 1));
            task.setDescription("Description for task " + (i + 1));
            task.setTaskType(taskTypes[i]);
            task.setProject(savedProject);

            MvcResult result = mockMvc.perform(post("/api/v1/task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(task)))
                    .andExpect(status().isCreated())
                    .andReturn();

            createdTasks[i] = objectMapper.readValue(
                    result.getResponse().getContentAsString(), Task.class);
        }

        MvcResult allTasksResult = mockMvc.perform(get("/api/v1/project/{projectId}/tasks", savedProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Task[] allTasks = objectMapper.readValue(
                allTasksResult.getResponse().getContentAsString(), Task[].class);

        assertThat(allTasks).hasSize(taskTypes.length);

        for (int i = 0; i < createdTasks.length; i++) {
            Task updateData = new Task();

            switch (i % 3) {
                case 0:
                    updateData.setTitle("Updated Bulk Task " + (i + 1));
                    break;
                case 1:
                    updateData.setTitle("Full Update " + (i + 1));
                    updateData.setDescription("Full update description");
                    updateData.setTaskType("UPDATED");
                    break;
                case 2:
                    updateData.setTitle(null);
                    updateData.setDescription(null);
                    updateData.setTaskType(null);
                    break;
            }

            mockMvc.perform(put("/api/v1/task/{id}", createdTasks[i].getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateData)))
                    .andExpect(status().isOk());
        }

        Project project2 = new Project();
        project2.setName("Target Project for Move");
        Project savedProject2 = projectRepository.save(project2);

        for (int i = 0; i < createdTasks.length; i += 2) {
            Task moveData = new Task();
            moveData.setTitle("Moved from bulk operations");
            moveData.setDescription("Task moved to project 2");
            moveData.setTaskType("MOVED");
            moveData.setProject(savedProject2);

            mockMvc.perform(put("/api/v1/task/{id}", createdTasks[i].getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(moveData)))
                    .andExpect(status().isOk());
        }

        MvcResult project1TasksResult = mockMvc.perform(get("/api/v1/project/{projectId}/tasks", savedProject.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Task[] project1Tasks = objectMapper.readValue(
                project1TasksResult.getResponse().getContentAsString(), Task[].class);

        MvcResult project2TasksResult = mockMvc.perform(get("/api/v1/project/{projectId}/tasks", savedProject2.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Task[] project2Tasks = objectMapper.readValue(
                project2TasksResult.getResponse().getContentAsString(), Task[].class);

        assertThat(project1Tasks.length + project2Tasks.length).isEqualTo(taskTypes.length);

        for (Task task : project1Tasks) {
            mockMvc.perform(delete("/api/v1/task/{id}", task.getId()))
                    .andExpect(status().isNoContent());
        }

        mockMvc.perform(get("/api/v1/project/{projectId}/tasks", savedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        mockMvc.perform(get("/api/v1/project/{projectId}/tasks", savedProject2.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(project2Tasks.length));
    }

    @Test
    @DisplayName("Should test task lifecycle within project context")
    @Transactional
    void shouldTestTaskLifecycleWithinProjectContext() throws Exception {
        Project project = new Project();
        project.setName("Task Lifecycle Project");
        Project savedProject = projectRepository.save(project);

        Task todoTask = new Task();
        todoTask.setTitle("New Feature Task");
        todoTask.setDescription("Implement new feature");
        todoTask.setTaskType("FEATURE");
        todoTask.setProject(savedProject);

        MvcResult createResult = mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoTask)))
                .andExpect(status().isCreated())
                .andReturn();

        Task createdTask = objectMapper.readValue(
                createResult.getResponse().getContentAsString(), Task.class);

        Task inProgressUpdate = new Task();
        inProgressUpdate.setTitle("New Feature Task - In Progress");
        inProgressUpdate.setDescription("Currently implementing new feature");
        inProgressUpdate.setTaskType("FEATURE");

        mockMvc.perform(put("/api/v1/task/{id}", createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inProgressUpdate)))
                .andExpect(status().isOk());

        Task bugUpdate = new Task();
        bugUpdate.setTitle("Bug Fix - Critical Issue");
        bugUpdate.setDescription("Fixed critical issue");
        bugUpdate.setTaskType("BUG");

        mockMvc.perform(put("/api/v1/task/{id}", createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bugUpdate)))
                .andExpect(status().isOk());

        MvcResult getResult = mockMvc.perform(get("/api/v1/task/{id}", createdTask.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Task finalTask = objectMapper.readValue(
                getResult.getResponse().getContentAsString(), Task.class);

        assertThat(finalTask.getTitle()).isEqualTo("Bug Fix - Critical Issue");
        assertThat(finalTask.getTaskType()).isEqualTo("BUG");
        assertThat(finalTask.getProject().getId()).isEqualTo(savedProject.getId());

        mockMvc.perform(delete("/api/v1/task/{id}", createdTask.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/task/{id}", createdTask.getId()))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/v1/project/{projectId}/tasks", savedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}