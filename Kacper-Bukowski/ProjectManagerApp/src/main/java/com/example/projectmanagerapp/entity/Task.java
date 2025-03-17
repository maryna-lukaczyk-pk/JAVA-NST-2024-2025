package com.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import com.example.projectmanagerapp.priority.PriorityLevel;

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

    @Transient
    private PriorityLevel priority;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTaskTypeEnum() {
        return taskTypeEnum;
    }

    public void setTaskTypeEnum(TaskType taskTypeEnum) {
        this.taskTypeEnum = taskTypeEnum;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
    }
}