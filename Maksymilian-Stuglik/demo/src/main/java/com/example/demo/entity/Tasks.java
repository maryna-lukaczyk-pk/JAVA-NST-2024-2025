package com.example.demo.entity;

import com.example.demo.priority.PriorityLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private taskType taskType;
    @Column(name = "projectId", insertable = false, updatable = false)
    private Long projectId;
    private String priority;

    private enum taskType {
        NEW,
        IN_PROGRESS,
        DONE;
    }
    @Transient
    private PriorityLevel priorityLevel;

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
        this.priority = priorityLevel.getPriority();
    }

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;
}