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

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    @OneToMany(mappedBy = "user")
    @com.fasterxml.jackson.annotation.JsonManagedReference("user-projects")
    private Set<ProjectUser> projectUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<ProjectUser> getProjectUser() {
        return projectUser;
    }

    public void setProjectUser(Set<ProjectUser> projectUser) {
        this.projectUser = projectUser;
    }
}
