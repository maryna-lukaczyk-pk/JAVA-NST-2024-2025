package s.kopec.ProjectManagerApp.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import s.kopec.ProjectManagerApp.entity.User;
import s.kopec.ProjectManagerApp.entity.Project;
import s.kopec.ProjectManagerApp.repository.UserRepository;
import s.kopec.ProjectManagerApp.repository.ProjectRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CucumberContextConfiguration
@SpringBootTest
@Transactional
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

        String jsonBody = String.format("{\"username\":\"%s\"}", uniqueUsername);

        MvcResult result = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk()) // Zmienione z isCreated() na isOk()
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(responseJson);
        Long userId = root.path("id").asLong();

        user = userRepository.findById(userId).orElseThrow();
    }

    @And("a new project exists")
    public void a_new_project_exists() throws Exception {
        String uniqueProjectName = "integrationProject_" + System.currentTimeMillis();

        String jsonBody = String.format("{\"name\":\"%s\"}", uniqueProjectName);

        MvcResult result = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk()) // Zmienione z isCreated() na isOk()
                .andReturn();

        project = projectRepository.findAll().stream()
                .filter(p -> uniqueProjectName.equals(p.getName()))
                .findFirst().orElseThrow();
    }

    @When("I assign the user to the project")
    public void i_assign_the_user_to_the_project() throws Exception {
        mvcResult = mockMvc.perform(put("/api/projects/add-user/" + project.getId() + "/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Then("the response status should be 200")
    public void the_response_status_should_be_200() {
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @And("the user should be assigned to the project")
    public void the_user_should_be_assigned_to_the_project() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/projects/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains("\"id\":" + user.getId()));
    }
}
