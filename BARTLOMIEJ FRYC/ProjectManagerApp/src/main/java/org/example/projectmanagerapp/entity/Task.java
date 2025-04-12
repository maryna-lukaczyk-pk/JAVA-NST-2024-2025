package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.priority.LowPriority;
import org.example.projectmanagerapp.priority.MediumPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    private task_type taskType;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Transient
    private PriorityLevel priority;

    private String priorityName;

    public void loadPriorityFromName() {
        switch (priorityName) {
            case "High" -> priority = new HighPriority();
            case "Medium" -> priority = new MediumPriority();
            case "Low" -> priority = new LowPriority();
            default -> priority = null;
        }
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
        this.priorityName = priority.getPriority();
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }
}