package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor

public class Task {
    public enum TaskType {
        HIGH,
        MEDIUM,
        LOW
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType type;

    @Transient
    private PriorityLevel priorityLevel;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public String getPriority() {
        return priorityLevel != null ? priorityLevel.getPriority() : "UNDEFINED";
    }

    public void setPriority(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }
}
