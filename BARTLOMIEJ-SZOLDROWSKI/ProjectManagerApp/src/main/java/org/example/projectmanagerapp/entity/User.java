package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    // Relacja z Task
    @OneToMany(mappedBy = "assignee")
    private List<Task> tasks;
}
