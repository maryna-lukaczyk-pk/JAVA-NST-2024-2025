package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example")
public class ProjectManagerAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerAppApplication.class, args);
    }
}