package com.example.entity;
import com.example.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToMany
    @JoinTable(name = "project_user", joinColumns = @JoinColumn(name="project_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))

    private List<User> users;


}
