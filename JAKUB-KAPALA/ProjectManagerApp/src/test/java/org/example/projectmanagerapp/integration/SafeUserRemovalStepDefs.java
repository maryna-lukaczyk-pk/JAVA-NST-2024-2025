package org.example.projectmanagerapp.integration;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

    private MockMvc mockMvc;
    private User user;
    private Project project;
    private MvcResult mvcResult;

    @Given("a user exists")
    public void a_user_exists() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        String uniqueUsername = "removalUser_" + System.currentTimeMillis();
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(uniqueUsername))
                .andExpect(status().isCreated());
        user = userRepository.findByUsername(uniqueUsername).orElseThrow();
    }

    @And("a project exists")
    public void a_project_exists() throws Exception {
        String uniqueProjectName = "removalProject_" + System.currentTimeMillis();
        mockMvc.perform(post("/api/projects/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(uniqueProjectName))
                .andExpect(status().isCreated());
        project = projectRepository.findAll().stream()
                .filter(p -> p.getName().startsWith("removalProject_"))
                .findFirst().orElseThrow();
    }

    @And("user is assigned to a project")
    public void user_is_assigned_to_a_project() throws Exception {
        mockMvc.perform(post("/api/projects/" + project.getId() + "/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @When("I delete the user")
    public void i_delete_the_user() throws Exception {
        mvcResult = mockMvc.perform(delete("/api/user/remove/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Then("user is deleted")
    public void user_is_deleted() {
        Assertions.assertEquals(204, mvcResult.getResponse().getStatus());
        Assertions.assertTrue(userRepository.findById(user.getId()).isEmpty());
    }

    @And("project does not preserve user assignment")
    public void project_does_not_preserve_user_assignment() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/projects/" + project.getId() + "/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertFalse(responseBody.contains(String.valueOf(user.getId())));
    }
}
