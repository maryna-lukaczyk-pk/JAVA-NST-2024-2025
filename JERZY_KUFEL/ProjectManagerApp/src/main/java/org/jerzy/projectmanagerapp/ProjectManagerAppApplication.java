package org.jerzy.projectmanagerapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan("org.jerzy.projectmanagerapp.entity")
@EnableJpaRepositories("org.jerzy.projectmanagerapp.repository")
public class ProjectManagerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerAppApplication.class, args);
    }

}
