package com.example.ProjectManagerApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.example.projectmanagerapp.entity")
@EnableJpaRepositories("com.example.projectmanagerapp.repository")

@SpringBootApplication
public class ProjectManagerAppAppilication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerAppAppilication.class, args);

    }

}
