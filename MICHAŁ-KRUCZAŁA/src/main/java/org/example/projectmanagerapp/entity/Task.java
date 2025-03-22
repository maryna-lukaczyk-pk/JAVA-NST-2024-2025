package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.PriorityLevel;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private Task_Type task_type;
    @ManyToOne
    private Project project;
    @Transient
    private PriorityLevel priority;


    private enum Task_Type {
        TYPE1, TYPE2, TYPE3
    }
}
