package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import org.example.projectmanagerapp.priority.HighPriority;
import org.example.projectmanagerapp.priority.LowPriority;
import org.example.projectmanagerapp.priority.MediumPriority;
import org.example.projectmanagerapp.priority.PriorityLevel;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    private task_type taskType;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Transient
    private PriorityLevel priority;

    private String priorityName;

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

    public task_type getTaskType() {
        return taskType;
    }

    public void setTaskType(task_type taskType) {
        this.taskType = taskType;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void loadPriorityFromName() {
        switch (priorityName) {
            case "High" -> priority = new HighPriority();
            case "Medium" -> priority = new MediumPriority();
            case "Low" -> priority = new LowPriority();
            default -> priority = null;
        }
    }

    public PriorityLevel getPriority() {
        return priority;
    }

    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
        this.priorityName = priority.getPriority();
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }
}
