package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
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
}
