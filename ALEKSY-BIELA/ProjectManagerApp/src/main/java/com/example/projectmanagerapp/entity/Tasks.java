package com.example.projectmanagerapp.entity;

import com.example.projectmanagerapp.service.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    @Transient
    private Priority task_type;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public void generatePriority(){
        int randomNum = (int)(Math.random() * 3);
        switch (randomNum) {
            case 1:
                this.task_type = new HighPriority().getPriority();
                break;
            case 2:
                this.task_type = new MediumPriority().getPriority();
                break;
            default:
                this.task_type = new LowPriority().getPriority();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Priority getTask_type() {
        return task_type;
    }

    public void setTask_type(Priority task_type) {
        this.task_type = task_type;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
