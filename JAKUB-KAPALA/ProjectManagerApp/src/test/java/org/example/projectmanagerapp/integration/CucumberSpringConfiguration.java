package org.example.projectmanagerapp.integration;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
    properties = {
      "spring.datasource.url=jdbc:postgresql://localhost:5432/task_manager_tests",
      "spring.datasource.username=postgres",
      "spring.datasource.password=postgres",
    })
public class CucumberSpringConfiguration {

  @Value("${local.server.port}")
  private int port;

}
