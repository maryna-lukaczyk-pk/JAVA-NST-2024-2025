package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Projects;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProjectApiIT extends BaseIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    void projectCrudFlow() throws Exception {
        // CREATE
        var c = mvc.perform(post("/api/project")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":\"Proj1\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        Projects created = om.readValue(c.getResponse().getContentAsString(), Projects.class);

        // READ ALL
        var all = mvc.perform(get("/api/project"))
                .andExpect(status().isOk())
                .andReturn();
        List<Projects> list = om.readValue(all.getResponse().getContentAsString(),
                om.getTypeFactory().constructCollectionType(List.class, Projects.class));
        assertThat(list).extracting("name").contains("Proj1");

        // READ BY ID
        mvc.perform(get("/api/project/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Proj1"));

        // UPDATE
        created.setName("Proj2");
        mvc.perform(put("/api/project/{id}", created.getId())
                        .contentType(APPLICATION_JSON)
                        .content(om.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Proj2"));

        // DELETE
        mvc.perform(delete("/api/project/{id}", created.getId()))
                .andExpect(status().isNoContent());

        // VERIFY DELETION → 404 Not Found
        mvc.perform(get("/api/project/{id}", created.getId()))
                .andExpect(status().isNotFound());
    }
}