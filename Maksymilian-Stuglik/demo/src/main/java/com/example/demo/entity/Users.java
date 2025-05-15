package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username; // no message

    // Explicit setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Explicit getter for username
    public String getUsername() {
        return username;
    }

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("user-projects")
    private Set<ProjectUsers> projectUsers;

    // Explicit setter for id
    public void setId(Long id) {
        this.id = id;
    }

    // Explicit getter for id
    public Long getId() {
        return id;
    }
}
