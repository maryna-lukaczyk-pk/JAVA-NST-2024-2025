package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project")

@Getter
@Setter
@NoArgsConstructor

public class Projects {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projects_id;

    @Column(name = "name", nullable = false)
    private String projects_name;
}