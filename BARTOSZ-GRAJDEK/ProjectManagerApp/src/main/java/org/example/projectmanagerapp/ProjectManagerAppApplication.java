package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("org.example.projectmanagerapp.entity")
@EnableJpaRepositories("org.example.projectmanagerapp.repository")
public class ProjectManagerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            TaskRepository taskRepository
    ) {
        return args -> {

            User user1 = new User("Alice");
            User user2 = new User("Bob");
            userRepository.save(user1);
            userRepository.save(user2);

            Project project1 = new Project("Spring Boot Project");
            Project project2 = new Project("Awesome App");
            projectRepository.save(project1);
            projectRepository.save(project2);

            user1.getProjects().add(project1);
            user1.getProjects().add(project2);
            userRepository.save(user1);

            user2.getProjects().add(project2);
            userRepository.save(user2);

            Task task1 = new Task("Implement login", "Create login functionality", TaskType.FEATURE);
            task1.setProject(project1);
            taskRepository.save(task1);

            Task task2 = new Task("Write documentation", "Add README and comments", TaskType.REFACTOR);
            task2.setProject(project1);
            taskRepository.save(task2);

            Task task3 = new Task("Fix minor bugs", "Small UI fixes", TaskType.BUG);
            task3.setProject(project2);
            taskRepository.save(task3);
        };
    }
}
