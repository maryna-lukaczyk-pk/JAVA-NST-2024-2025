package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CinemaHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer capacity;
    private Boolean has3D;

    @OneToMany(mappedBy = "cinemaHall")
    private List<Screening> screenings;

    // Constructors, getters, setters
    public CinemaHall() {}

    public CinemaHall(String name, Integer capacity, Boolean has3D) {
        this.name = name;
        this.capacity = capacity;
        this.has3D = has3D;
    }
}
