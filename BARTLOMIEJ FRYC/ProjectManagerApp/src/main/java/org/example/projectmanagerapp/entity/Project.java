package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import java.util.Set;
@Entity

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "project_id")
    private Set<Task> tasks;

    @ManyToMany
    private Set<Users> users;
}