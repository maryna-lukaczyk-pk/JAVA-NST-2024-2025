import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.example.projectmanagerapp.ProjectManagerAppApplication;

// Test sprawdzający, czy metoda main aplikacji uruchamia się bez wyjątków
public class ProjectManagerAppTest {

    @Test
    void mainMethodRunsWithoutExceptions() {
        assertDoesNotThrow(() -> ProjectManagerAppApplication.main(new String[]{}));
    }
}