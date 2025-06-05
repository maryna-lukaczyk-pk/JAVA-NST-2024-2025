package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime reservationTime;
    private Boolean isPaid;
    private Double totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "screening_id")
    private Screening screening;

    @OneToMany(mappedBy = "reservation")
    private List<Ticket> tickets;

    // Constructors
    public Reservation() {}

    public Reservation(LocalDateTime reservationTime, Boolean isPaid,
                       Double totalPrice, User user, Screening screening) {
        this.reservationTime = reservationTime;
        this.isPaid = isPaid;
        this.totalPrice = totalPrice;
        this.user = user;
        this.screening = screening;
    }
}