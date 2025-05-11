package org.example.projectmanagerapp.entity;

import org.example.projectmanagerapp.priority.PriorityLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor

// Reprezentuje zadanie przypisane do projektu
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Task(String title, String description, TaskType taskType, Project project) {
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.project = project;
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    // Priorytet zadania, ustawiany dynamicznie, nie zapisywany w bazie
    @Transient
    @JsonIgnore
    private PriorityLevel priorityLevel = PriorityLevel.UNDEFINED;

    @JsonProperty("priority")
    public String getPriority() { return priorityLevel.getPriority(); }

    @JsonProperty("priority")
    public void setPriority(String value) {
        switch (value.toUpperCase()) {
            case "LOW" -> priorityLevel = new org.example.projectmanagerapp.priority.LowPriority();
            case "MEDIUM" -> priorityLevel = new org.example.projectmanagerapp.priority.MediumPriority();
            case "HIGH" -> priorityLevel = new org.example.projectmanagerapp.priority.HighPriority();
            default -> priorityLevel = PriorityLevel.UNDEFINED;
        }
    }
}