package com.example.projectmanagerapp.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Represents a project in the project management system.")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the project, generated automatically.",
            example = "101",
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Name of the project.",
            example = "Test123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_app_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Schema(description = "Set of users associated with this project. " +
            "This is typically a read-only view when fetching a project; " +
            "associations are managed through user or association APIs, or by adding users to the project.",
            accessMode = Schema.AccessMode.READ_ONLY, // Or READ_WRITE if you update users via Project
            nullable = false)
    private Set<User> users = new HashSet<>();

    public Project(String name) {
        this.name = name;
    }

    // Helper methods for managing bidirectional relationship
    public void addUser(User user) {
        this.users.add(user);
        user.getProjects().add(this); // This assumes User.projects is accessible and modifiable here
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.getProjects().remove(this);
    }
}