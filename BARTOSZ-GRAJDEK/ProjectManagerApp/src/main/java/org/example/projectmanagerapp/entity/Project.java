package org.example.projectmanagerapp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "projects")
    @JsonManagedReference
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonManagedReference
    private List<Task> tasks = new ArrayList<>();

    public Project() {
    }

    public Project(String name) {
        this.name = name;
    }
}