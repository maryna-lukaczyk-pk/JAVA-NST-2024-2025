package com.example.projectmanagerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.example.projectmanagerapp.entity")
@EnableJpaRepositories("com.example.projectmanagerapp.repository")
public class ProjectManagerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerAppApplication.class, args);
    }

}
