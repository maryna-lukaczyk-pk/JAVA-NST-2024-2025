package com.example.projectmanagerapp;

import com.example.projectmanagerapp.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class ApplicationIntegrationTest {

    @Test
    public void contextLoads() {
        // This test will fail if the application context cannot be loaded
        // No assertions needed as the test will fail if the context fails to load
    }

    @Test
    public void mainMethodStartsApplication() {
        // Test that the main method can be called without errors
        ProjectManagerAppApplication.main(new String[]{});
    }
}