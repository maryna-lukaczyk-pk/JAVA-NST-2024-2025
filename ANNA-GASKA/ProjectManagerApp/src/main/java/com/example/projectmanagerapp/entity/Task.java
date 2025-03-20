package com.example.projectmanagerapp.entity;

import java.util.Set;
import com.example.projectmanagerapp.priority.PriorityLevel;
import com.example.projectmanagerapp.priority.HighPriority;
import com.example.projectmanagerapp.priority.MediumPriority;
import com.example.projectmanagerapp.priority.LowPriority;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private Task_type task_type;

    private String priorityName;

    @Transient
    private PriorityLevel priorityLevel;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Projects project;

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

    public Task_type getTask_type() {
        return task_type;
    }

    public void setTask_type(Task_type task_type) {
        this.task_type = task_type;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects project) {
        this.project = project;
    }


    public PriorityLevel getPriority() {
        if(this.priorityLevel == null && this.priorityName != null) {
            switch (priorityName){
                case "High":
                    this.priorityLevel = new HighPriority();
                    break;
                case "Medium":
                    this.priorityLevel = new MediumPriority();
                    break;
                case "Low":
                    this.priorityLevel = new LowPriority();
                    break;
                default:
                    this.priorityLevel = new MediumPriority();
                    break;
            }
        }
        return this.priorityLevel;
    }

    public void setPriority(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
        if (priorityLevel != null) {
            this.priorityName = priorityLevel.getPriority();
        } else {
            this.priorityName = null;
        }
    }

}