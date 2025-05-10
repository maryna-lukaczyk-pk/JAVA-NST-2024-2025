package priority;

import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class HighPriorityTest {
    @Test
    void getPriority_returnsHigh() {
        PriorityLevel p = new HighPriority();
        assertThat(p.getPriority()).isEqualTo("High Priority");
    }
}
