package org.example.projectmanagerapp.entity;


import jakarta.persistence.*;

@Entity

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Long project_id;
    private task_type taskType;

   @ManyToOne
   @JoinColumn(name = "project")
    private Project project;
}
