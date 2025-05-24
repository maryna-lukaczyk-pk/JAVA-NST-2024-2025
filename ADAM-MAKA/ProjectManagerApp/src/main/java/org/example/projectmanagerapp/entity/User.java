package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Getter
    private String username;

    @Setter
    @Getter
    @Column(nullable = false)
    private String email;

    @ManyToMany(mappedBy = "users")
    private Set<Project> projects;
}