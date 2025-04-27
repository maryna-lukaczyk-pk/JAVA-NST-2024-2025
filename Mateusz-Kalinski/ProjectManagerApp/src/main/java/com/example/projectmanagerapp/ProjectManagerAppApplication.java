package com.example.projectmanagerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("org.example.projectmanager.entity")
@EnableJpaRepositories("org.example.projectmanager.repository")

public class ProjectManagerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerAppApplication.class, args);
    }

}
