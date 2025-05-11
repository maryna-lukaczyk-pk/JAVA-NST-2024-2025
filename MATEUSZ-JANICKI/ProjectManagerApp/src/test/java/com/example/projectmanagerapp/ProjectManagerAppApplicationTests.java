package com.example.projectmanagerapp;

import org.example.projectmanagerapp.ProjectManagerAppApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
class ProjectManagerAppApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true);
    }

}
