package org.example.projectmanagerapp.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        glue = "org.example.projectmanagerapp.integration",
        plugin = {"pretty", "summary", "html:target/cucumber-reports.html"}
)
public class CucumberIntegrationTestRunner {
    // This class is used only as an entry point for Cucumber tests
}
