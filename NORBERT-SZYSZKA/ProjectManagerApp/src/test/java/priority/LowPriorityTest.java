package priority;

import org.example.projectmanagerapp.priority.LowPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class LowPriorityTest {
    @Test
    void returnsLow() {
        PriorityLevel p = new LowPriority();
        assertThat(p.getPriority()).isEqualTo("Low Priority");
    }
}
