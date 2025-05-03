import org.example.projectmanagerapp.entity.LowPriority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LowPriorityTest {

    LowPriority lowPriority = new LowPriority();

    @Test
    @DisplayName("Should return LOW PRIORITY")
    void getPriorityLevel_shouldReturnLowPriority() {

        String expected = "LOW PRIORITY";
        String actual = lowPriority.getPriorityLevel();

        assertEquals(expected, actual);
    }
}
