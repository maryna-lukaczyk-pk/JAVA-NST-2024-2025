package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // ← DODANE POLE

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_level", nullable = false)
    private PriorityLevel priorityLevel;

    public Task() {
    }

    public Task(String name, String description, Project project, PriorityLevel priorityLevel) {
        this.name = name;
        this.description = description;
        this.project = project;
        this.priorityLevel = priorityLevel;
    }

    public Long getId() {
        return id;
    }

    public String getName() { // ← GETTER
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Project getProject() {
        return project;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) { // ← SETTER
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public enum PriorityLevel {
        LOW,
        MEDIUM,
        HIGH
    }
}
