import com.example.ProjectManagerAppApplication;
import com.example.Task;
import com.example.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("This is a test task");

        Task savedTask = taskRepository.save(task);
        assertNotNull(savedTask.getId());
    }
}