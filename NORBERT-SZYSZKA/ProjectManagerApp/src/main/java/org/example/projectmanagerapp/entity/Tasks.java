package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;     // do zmiany

    @Enumerated(EnumType.STRING)
    private TaskType task_type;         // do zmiany

    @Column(nullable = false)
    private int project_id;
}