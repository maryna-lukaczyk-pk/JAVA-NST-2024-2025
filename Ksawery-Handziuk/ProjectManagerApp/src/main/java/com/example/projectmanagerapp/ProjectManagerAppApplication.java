// src/main/java/com/example/projectmanagerapp/ProjectManagerAppApplication.java
package com.example.projectmanagerapp; // Zmieniono pakiet

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Skanowanie encji w określonym pakiecie
@EntityScan("com.example.projectmanagerapp.entity")
// Włączenie repozytoriów JPA w określonym pakiecie
@EnableJpaRepositories("com.example.projectmanagerapp.repository")
@SpringBootApplication
public class ProjectManagerAppApplication { // Zmieniono nazwę klasy

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerAppApplication.class, args); // Zmieniono nazwę klasy
    }
}