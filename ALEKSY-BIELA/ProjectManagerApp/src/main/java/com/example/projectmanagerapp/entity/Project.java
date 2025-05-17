package com.example.projectmanagerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    private Long id;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "project_users",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<Users> projectUsers;
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    Set<Tasks> projectTasks;

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

    public Set<Users> getProjectUsers() {
        return projectUsers;
    }

    public void setProjectUsers(Set<Users> projectUsers) {
        this.projectUsers = projectUsers;
    }

    public Set<Tasks> getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(Set<Tasks> projectTasks) {
        this.projectTasks = projectTasks;
    }
}
