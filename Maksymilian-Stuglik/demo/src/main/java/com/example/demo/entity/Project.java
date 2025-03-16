package com.example.demo.entity;

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
    private String name; // no message

    @OneToMany(mappedBy = "project")
    private Set<ProjectUsers> projectUsers;
//D
    @OneToMany(mappedBy = "project")
    private Set<Tasks> tasks;
}