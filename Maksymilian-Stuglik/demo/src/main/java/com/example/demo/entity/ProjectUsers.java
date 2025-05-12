package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"projectId", "userId"}))
public class ProjectUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projectId")
    @JsonBackReference("project-users")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonBackReference("user-projects")
    private Users user;

    // Explicit setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Explicit getter for id
    public Long getId() {
        return id;
    }

    // Explicit setter for project
    public void setProject(Project project) {
        this.project = project;
    }

    // Explicit getter for project
    public Project getProject() {
        return project;
    }

    // Explicit setter for user
    public void setUser(Users user) {
        this.user = user;
    }

    // Explicit getter for user
    public Users getUser() {
        return user;
    }
}
