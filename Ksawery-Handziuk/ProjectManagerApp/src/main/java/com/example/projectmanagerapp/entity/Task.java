package com.example.projectmanagerapp.entity;
import com.example.projectmanagerapp.PriorityLevel;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Transient
    private PriorityLevel priority;

    // Gettery i Settery
    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority != null ? priority.getPriority() : "UNDEFINED";
    }
}

