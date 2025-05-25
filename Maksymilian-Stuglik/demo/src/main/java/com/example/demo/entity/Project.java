package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    // Explicit setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Explicit getter for name
    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "project")
    @JsonManagedReference("project-users")
    private Set<ProjectUsers> projectUsers;

    @OneToMany(mappedBy = "project")
    @JsonManagedReference("project-tasks")
    private Set<Tasks> tasks;

    // Explicit setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Explicit getter for id
    public Long getId() {
        return id;
    }
}
