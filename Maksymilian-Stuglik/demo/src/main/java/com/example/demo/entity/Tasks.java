package com.example.demo.entity;

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
    private Long projectId;

    private enum taskType {
        NEW,
        IN_PROGRESS,
        DONE;
    }

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;
}