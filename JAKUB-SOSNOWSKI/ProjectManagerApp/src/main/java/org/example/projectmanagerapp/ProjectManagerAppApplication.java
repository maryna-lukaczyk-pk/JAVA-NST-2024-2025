package org.example.projectmanagerapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@EntityScan("org.example.projectmanagerapp.entity")
@EnableJpaRepositories("org.example.projectmanagerapp.repository")
@SpringBootApplication

public class ProjectManagerAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerAppApplication.class, args);
    }

}
