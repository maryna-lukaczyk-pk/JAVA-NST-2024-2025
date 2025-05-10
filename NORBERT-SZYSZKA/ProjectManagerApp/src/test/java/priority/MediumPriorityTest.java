package priority;

import org.example.projectmanagerapp.priority.MediumPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class MediumPriorityTest {
    @Test
    void returnsMedium() {
        PriorityLevel p = new MediumPriority();
        assertThat(p.getPriority()).isEqualTo("Medium Priority");
    }
}
