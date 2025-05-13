package org.example.projectmanagerapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

// Testy jednostkowe dla głównej klasy aplikacji ProjectManagerApp
@SpringBootTest
class ProjectManagerAppApplicationTests {

    @Test
    void mainMethodRunsWithoutExceptions() {
        assertDoesNotThrow(() -> ProjectManagerAppApplication.main(new String[]{}));
    }
}