package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Integer duration; // in minutes
    private LocalDate releaseDate;
    private String genre;
    private String director;
    private Integer ageRestriction;

    @OneToMany(mappedBy = "movie")
    private List<Screening> screenings;

    // Constructors
    public Movie() {}

    public Movie(String title, String description, Integer duration,
                 LocalDate releaseDate, String genre, String director,
                 Integer ageRestriction) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.director = director;
        this.ageRestriction = ageRestriction;
    }
}