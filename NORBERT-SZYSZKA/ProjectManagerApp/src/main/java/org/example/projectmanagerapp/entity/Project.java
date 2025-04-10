package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "project_user",
        joinColumns = @JoinColumn(
            name = "project_id",
            foreignKey = @ForeignKey(name = "fk_project_user_project")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_project_user_user")
        )
    )
    private Set<User> users;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Task> tasks;
}