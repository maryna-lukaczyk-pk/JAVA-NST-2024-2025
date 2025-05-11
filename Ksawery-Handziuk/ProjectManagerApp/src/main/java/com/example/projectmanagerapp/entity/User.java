package com.example.projectmanagerapp.entity; // CRITICAL

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Represents a user in the project management system.")
public class User { // CRITICAL: public class User

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the user, generated automatically.",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Unique username for the user. Must be unique across all users.",
            example = "jacek123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            nullable = false)
    private String username;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @Schema(description = "Set of projects the user is associated with.",
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = false)
    private Set<Project> projects = new HashSet<>(); // Project.java must also be correct and in the same package

    public User(String username) {
        this.username = username;
    }
}