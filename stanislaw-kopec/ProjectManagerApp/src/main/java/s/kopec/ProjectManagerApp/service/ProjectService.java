package s.kopec.ProjectManagerApp.service;

import org.springframework.stereotype.Service;
import s.kopec.ProjectManagerApp.entity.Project;
import s.kopec.ProjectManagerApp.entity.User;
import s.kopec.ProjectManagerApp.repository.ProjectRepository;
import s.kopec.ProjectManagerApp.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> findProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProjectById(Long id) {
        projectRepository.deleteById(id);
    }

    public void updateProjectName(Long id, String newName) {
        if (projectRepository.existsById(id)) {
            Project myProject = projectRepository.getReferenceById(id);
            myProject.setName(newName);
            projectRepository.save(myProject);
        }
    }

    public void addNewUser(Long projectId, Long newUserId) {
        if (projectRepository.existsById(projectId) && userRepository.existsById(newUserId)) {
            Project myProject = projectRepository.getReferenceById(projectId);
            User myUser = userRepository.getReferenceById(newUserId);
            Set<User> mySetOfUsers = myProject.getUsers();
            mySetOfUsers.add(myUser);
            myProject.setUsers(mySetOfUsers);
            projectRepository.save(myProject);
        }
    }

}
