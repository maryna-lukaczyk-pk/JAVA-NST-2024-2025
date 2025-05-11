package com.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Transient // Nie mapuj tego pola do bazy danych
    private PriorityLevel priorityLevel;

    public String getPriority() {
        if (priorityLevel != null) {
            return priorityLevel.getPriority();
        }
        return null; // Or handle default priority
    }
}