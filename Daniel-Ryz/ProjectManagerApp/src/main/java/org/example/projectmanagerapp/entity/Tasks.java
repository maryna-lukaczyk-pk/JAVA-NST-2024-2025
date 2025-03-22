package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Transient
    private String dynamicPriority;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

}

