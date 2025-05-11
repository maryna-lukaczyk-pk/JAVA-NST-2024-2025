package org.example.projectmanagerapp.integration;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest
@ContextConfiguration
public class AssignUserToProjectStepDefs {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    private MockMvc mockMvc;
    private User user;
    private Project project;
    private MvcResult mvcResult;

    @Given("a new user exists")
    public void a_new_user_exists() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        String uniqueUsername = "integrationUser_" + System.currentTimeMillis();
        MvcResult result = mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(uniqueUsername))
                .andExpect(status().isCreated())
                .andReturn();
        System.out.println("Register user response: " + result.getResponse().getContentAsString());
        user = userRepository.findByUsername(uniqueUsername).orElseThrow();
    }

    @And("a new project exists")
    public void a_new_project_exists() throws Exception {
        String uniqueProjectName = "integrationProject_" + System.currentTimeMillis();
        MvcResult result = mockMvc.perform(post("/api/projects/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(uniqueProjectName))
                .andExpect(status().isCreated())
                .andReturn();
        project = projectRepository.findAll().stream()
                .filter(p -> "integrationProject".equals(p.getName()))
                .findFirst().orElseThrow();
    }

    @When("I assign the user to the project")
    public void i_assign_the_user_to_the_project() throws Exception {
        mvcResult = mockMvc.perform(post("/api/projects/" + project.getId() + "/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Then("the response status should be 200")
    public void the_response_status_should_be_200() {
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @And("the user should be assigned to the project")
    public void the_user_should_be_assigned_to_the_project() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/projects/" + project.getId() + "/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains(String.valueOf(user.getId())));
    }

    @And("the project should be assigned to the user")
    public void the_project_should_be_assigned_to_the_user() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/user/" + user.getId() + "/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains(String.valueOf(project.getId())));
    }
}
