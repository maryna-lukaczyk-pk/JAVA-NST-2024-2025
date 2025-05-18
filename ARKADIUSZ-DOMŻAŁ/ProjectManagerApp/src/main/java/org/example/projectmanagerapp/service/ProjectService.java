package org.example.projectmanagerapp.service;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Projects> getAllProjects() {
        return projectRepository.findAll();
    }

    public Projects createProject(Projects project){
        return projectRepository.save(project);
    }

    //metoda PUT
    public Optional<Projects> updateProject(Long id, Projects updatedProject){
        return projectRepository.findById(id).map(project -> {
            project.setName(updatedProject.getName());
            project.setUsers(updatedProject.getUsers());
            project.setTasks(updatedProject.getTasks());
            return projectRepository.save(project);
        });
    }

    //metoda DELETE
    public boolean deleteProject(Long id){
        if(projectRepository.existsById(id)){
            projectRepository.deleteById(id);
            return true; // jak usunieto to zwraca 0
        }else{
            return false; // jak nie usunieto to zwraca 1
        }
    }

    public Optional<Projects> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public boolean assignUserToProject(Long projectId, Long userId) {
        Optional<Projects> projectOpt = projectRepository.findById(projectId);
        Optional<Users> userOpt = userRepository.findById(userId);

        if (projectOpt.isPresent() && userOpt.isPresent()) {
            Projects project = projectOpt.get();
            Users user = userOpt.get();

            // dodajemy użytkownika do projektu
            project.getUsers().add(user);

            // zapisujemy projekt, aby utrwalić relację
            projectRepository.save(project);
            return true;
        }

        return false;
    }

    public List<Users> getUsersOfProject(Long projectId) {
        return projectRepository.findById(projectId)
                .map(Projects::getUsers)
                .orElse(null);
    }
}

