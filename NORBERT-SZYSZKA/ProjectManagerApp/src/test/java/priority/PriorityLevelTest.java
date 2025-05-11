package priority;

import org.assertj.core.api.Assertions;
import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.priority.LowPriority;
import org.example.projectmanagerapp.priority.MediumPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class PriorityLevelTest {

    static Stream<Arguments> priorityProvider() {
        return Stream.of(
                Arguments.of(new HighPriority(), "High Priority"),
                Arguments.of(new MediumPriority(), "Medium Priority"),
                Arguments.of(new LowPriority(), "Low Priority")
        );
    }

    @ParameterizedTest(name = "{index} => {0}.getPriority() == \"{1}\"")
    @MethodSource("priorityProvider")
    void getPriority_returnsExpected(PriorityLevel priorityLevel, String expected) {
        Assertions.assertThat(priorityLevel.getPriority()).isEqualTo(expected);
    }
}
