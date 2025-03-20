package org.example.projectmanager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Task() { }

    public Task(String title, String description, TaskType taskType) {
        this.title = title;
        this.description = description;
        this.taskType = taskType;
    }
}
