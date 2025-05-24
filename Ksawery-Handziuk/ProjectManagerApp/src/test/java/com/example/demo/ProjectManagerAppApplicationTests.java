// src/test/java/com/example/projectmanagerapp/ProjectManagerAppApplicationTests.java
package com.example.demo;
 // Zmieniono pakiet

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.projectmanagerapp.ProjectManagerAppApplication;
// Testy dla głównej klasy aplikacji Spring Boot
@SpringBootTest(classes = ProjectManagerAppApplication.class) // Zmieniono klasę na poprawną nazwę
class ProjectManagerAppApplicationTests { // Zmieniono nazwę klasy testowej

    // Podstawowy test sprawdzający, czy kontekst Springa ładuje się poprawnie
    @Test
    void contextLoads() {
    }
}