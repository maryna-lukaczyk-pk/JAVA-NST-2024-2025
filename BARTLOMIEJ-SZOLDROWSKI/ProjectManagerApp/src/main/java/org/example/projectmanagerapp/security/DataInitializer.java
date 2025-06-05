package org.example.projectmanagerapp.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User("admin", encoder.encode("admin123"), "admin@example.com", User.UserRole.ADMIN);
                userRepository.save(admin);
            }

            // Tworzenie zwykłego użytkownika
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User(
                        "user",
                        encoder.encode("user123"),
                        "user@example.com",
                        User.UserRole.USER
                );
                userRepository.save(user);
            }
        };
    }
}

