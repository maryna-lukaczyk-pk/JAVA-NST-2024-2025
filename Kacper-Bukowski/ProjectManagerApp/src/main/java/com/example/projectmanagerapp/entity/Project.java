package com.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @jakarta.persistence.Column(length = 256)
    private String name;

    @ManyToMany(mappedBy = "projects")
    private Set<User> users;

    @OneToMany(mappedBy = "project")
    private Set<Task> tasks;
}