package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String task_type;
/*
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User assignee;
  */
    @ManyToOne
    @JoinColumn(name = "project_id")  // Pole 'project' w encji Task
    private Project project;

    @Transient // Pole nie będzie zapisywane w bazie danych
    private PriorityLevel priorityLevel; // Dynamiczny priorytet

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getPriority() {
        return priorityLevel != null ? priorityLevel.getPriority() : "UNDEFINED";
    }
}