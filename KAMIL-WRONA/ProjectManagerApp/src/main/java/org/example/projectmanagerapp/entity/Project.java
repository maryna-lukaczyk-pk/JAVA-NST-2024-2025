package org.example.projectmanagerapp.entity;

import jakarta.persistance.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

