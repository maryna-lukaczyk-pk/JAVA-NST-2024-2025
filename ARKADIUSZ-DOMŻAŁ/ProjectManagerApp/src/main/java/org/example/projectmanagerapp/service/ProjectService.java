package org.example.projectmanagerapp.service;
import org.example.projectmanagerapp.entity.Users;
import org.springframework.stereotype.Service;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService (ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
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
}

