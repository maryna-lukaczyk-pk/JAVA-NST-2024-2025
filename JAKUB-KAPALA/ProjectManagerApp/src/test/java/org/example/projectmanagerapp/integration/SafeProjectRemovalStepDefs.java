package org.example.projectmanagerapp.integration;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
public class SafeProjectRemovalStepDefs {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ScenarioContext scenarioContext;

    private MockMvc mockMvc;
    private MvcResult mvcResult;

    @And("a task exists for the project")
    public void a_task_exists_for_the_project() throws Exception {
        if (mockMvc == null && context != null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        }
        String taskJson = "{" +
                "\"title\":\"removalTask_" + System.currentTimeMillis() + "\"," +
                "\"description\":\"desc\"," +
                "\"taskType\":\"HIGH\"," +
                "\"projectId\":" + scenarioContext.project.getId() + "}";
        mockMvc.perform(post("/api/tasks/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isCreated());
        scenarioContext.task = taskRepository.findAll().stream()
                .filter(t -> t.getProject().getId().equals(scenarioContext.project.getId()))
                .findFirst().orElseThrow();
    }

    @When("I delete the project")
    public void i_delete_the_project() throws Exception {
        mvcResult = mockMvc.perform(delete("/api/projects/remove/" + scenarioContext.project.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Then("project is deleted")
    public void project_is_deleted() {
        Assertions.assertEquals(204, mvcResult.getResponse().getStatus());
        Assertions.assertTrue(projectRepository.findById(scenarioContext.project.getId()).isEmpty());
    }

    @And("user does not preserve project assignment")
    public void user_does_not_preserve_project_assignment() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/user/" + scenarioContext.user.getId() + "/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertFalse(responseBody.contains(String.valueOf(scenarioContext.project.getId())));
    }

    @And("task is deleted")
    public void task_is_deleted() {
        Assertions.assertTrue(taskRepository.findById(scenarioContext.task.getId()).isEmpty());
    }
}
