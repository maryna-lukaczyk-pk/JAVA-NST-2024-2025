package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", columnDefinition = "ENUM('HIGH_PRIORITY', 'MEDIUM_PRIORITY', 'LOW_PRIORITY')")
    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public enum TaskType {
        HIGH_PRIORITY,
        MEDIUM_PRIORITY,
        LOW_PRIORITY
    }
}