package com.example.projectmanagerapp.entity;

import jakarta.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @jakarta.persistence.Column(length = 256)
    private String title;
    @jakarta.persistence.Column(columnDefinition = "TEXT")
    private String description;
    private TaskType taskTypeEnum;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
}