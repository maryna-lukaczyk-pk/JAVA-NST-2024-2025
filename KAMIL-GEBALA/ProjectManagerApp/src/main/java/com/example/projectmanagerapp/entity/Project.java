package  com.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "project")
    @com.fasterxml.jackson.annotation.JsonManagedReference("project-users")
    private Set<ProjectUser> projectUser;

    @OneToMany(mappedBy = "project")
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private Set<Tasks> tasks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProjectUser> getProjectUser() {
        return projectUser;
    }

    public void setProjectUser(Set<ProjectUser> projectUser) {
        this.projectUser = projectUser;
    }

    public Set<Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Tasks> tasks) {
        this.tasks = tasks;
    }
}
