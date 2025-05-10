package s.kopec.ProjectManagerApp.service;

import org.springframework.stereotype.Service;
import s.kopec.ProjectManagerApp.entity.Project;
import s.kopec.ProjectManagerApp.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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

}
