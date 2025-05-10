package com.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tasks")
@Getter
@Setter
@NoArgsConstructor

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, unique=true)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType task_type;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

    private String priority;
}
