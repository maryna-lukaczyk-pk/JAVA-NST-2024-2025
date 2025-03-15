package com.example.projectmanagerapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Project {
    @Id
    private Long id;
    private String name;
}