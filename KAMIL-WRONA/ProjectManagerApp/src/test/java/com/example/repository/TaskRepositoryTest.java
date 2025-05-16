import com.example.ProjectManagerAppApplication;
import com.example.entity.Task;
import com.example.repository.TaskRepository;
import com.example.entity.Project;
import com.example.repository.ProjectRepository;
import com.example.entity.User;
import com.example.repository.UserRepository;
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

        User user = userRepository.findByUsername("Test User");
        if (user == null) {
            user = new User();
            user.setUsername("Test User");
            user = userRepository.save(user);
        }


        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("This is a test task");
        task.setProject(savedProject);
        task.setUser(user);
        task.setTaskType("TASK");
        taskRepository.save(task);

        Task savedTask = taskRepository.save(task);
        assertNotNull(savedTask.getId());
        assertEquals(savedProject.getId(), savedTask.getProject().getId());
    }



}