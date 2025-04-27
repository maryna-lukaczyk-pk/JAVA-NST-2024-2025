package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tasks")
public class Task {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Getter
    private String title;

    @Setter
    @Getter
    @Column(columnDefinition = "TEXT")
    private String description;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TaskType taskType;

    @Setter
    @Getter
    @ManyToOne
    private Project project;
}