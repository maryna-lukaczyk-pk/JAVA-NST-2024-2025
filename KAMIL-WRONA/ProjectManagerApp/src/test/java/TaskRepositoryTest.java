import com.example.ProjectManagerAppApplication;
import com.example.Task;
import com.example.TaskRepository;
import com.example.Project;
import com.example.ProjectRepository;
import com.example.User;
import com.example.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
public class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateTask() {

        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("This is a test project");
        Project savedProject = projectRepository.save(project);
        projectRepository.flush();

        User existingUser = userRepository.findByUsername("Test User");
        if (existingUser != null) {
            userRepository.delete(existingUser);
        }
        User user = new User();
        user.setUsername("Test User");
        User savedUser = userRepository.save(user);
        userRepository.flush();

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
        task.setProject(savedProject);
        task.setUser(savedUser);

        Task savedTask = taskRepository.save(task);
        assertNotNull(savedTask.getId());
        assertEquals(savedProject.getId(), savedTask.getProject().getId());
    }
}