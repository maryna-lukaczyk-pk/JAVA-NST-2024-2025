package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;
    private Boolean is3D;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "cinema_hall_id")
    private CinemaHall cinemaHall;

    @OneToMany(mappedBy = "screening")
    private List<Reservation> reservations;

    // Constructors
    public Screening() {}

    public Screening(LocalDateTime startTime, LocalDateTime endTime,
                     Double price, Boolean is3D, Movie movie,
                     CinemaHall cinemaHall) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.is3D = is3D;
        this.movie = movie;
        this.cinemaHall = cinemaHall;
    }
}