package org.example.projectmanagerapp.integration;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
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
public class SafeUserRemovalStepDefs {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ScenarioContext scenarioContext;

    private MockMvc mockMvc;
    private MvcResult mvcResult;

    @Given("a user exists")
    public void a_user_exists() throws Exception {
        if (mockMvc == null && context != null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        }
        scenarioContext.uniqueUsername = "removalUser_" + System.currentTimeMillis();
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(scenarioContext.uniqueUsername))
                .andExpect(status().isCreated());
        scenarioContext.user = userRepository.findByUsername(scenarioContext.uniqueUsername).orElseThrow();
    }

    @And("a project exists")
    public void a_project_exists() throws Exception {
        if (mockMvc == null && context != null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        }
        scenarioContext.uniqueProjectName = "removalProject_" + System.currentTimeMillis();
        mockMvc.perform(post("/api/projects/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(scenarioContext.uniqueProjectName))
                .andExpect(status().isCreated());
        scenarioContext.project = projectRepository.findAll().stream()
                .filter(p -> scenarioContext.uniqueProjectName.equals(p.getName()))
                .findFirst().orElseThrow();
    }

    @And("user is assigned to a project")
    public void user_is_assigned_to_a_project() throws Exception {
        mockMvc.perform(post("/api/projects/" + scenarioContext.project.getId() + "/users/" + scenarioContext.user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @When("I delete the user")
    public void i_delete_the_user() throws Exception {
        mvcResult = mockMvc.perform(delete("/api/user/remove/" + scenarioContext.user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Then("user is deleted")
    public void user_is_deleted() {
        Assertions.assertEquals(204, mvcResult.getResponse().getStatus());
        Assertions.assertTrue(userRepository.findById(scenarioContext.user.getId()).isEmpty());
    }

    @And("project does not preserve user assignment")
    public void project_does_not_preserve_user_assignment() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/projects/" + scenarioContext.project.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertFalse(responseBody.contains(String.valueOf(scenarioContext.user.getId())));
    }
}
