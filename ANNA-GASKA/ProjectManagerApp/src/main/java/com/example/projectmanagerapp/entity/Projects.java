package com.example.projectmanagerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(mappedBy = "projects")
    @JsonIgnoreProperties("projects")
    private Set<User> users = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(
            mappedBy = "project",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnoreProperties("project")
    private Set<Task> tasks = new HashSet<>();

    public void setName(String name) {
        this.name = name;
    }

    public void addUser(User user) {
        this.users.add(user);
        user.getProjects().add(this);
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setProject(this);
    }
}
