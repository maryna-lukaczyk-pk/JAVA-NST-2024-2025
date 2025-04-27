package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TaskType taskType; // Using camelCase for field name

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}