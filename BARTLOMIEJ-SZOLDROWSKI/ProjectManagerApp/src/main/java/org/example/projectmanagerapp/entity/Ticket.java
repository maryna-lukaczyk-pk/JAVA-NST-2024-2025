package org.example.projectmanagerapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;
    private String ticketType; // "ADULT", "CHILD", "STUDENT", etc.
    private Double price;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    // Constructors
    public Ticket() {}

    public Ticket(String seatNumber, String ticketType, Double price,
                  Reservation reservation) {
        this.seatNumber = seatNumber;
        this.ticketType = ticketType;
        this.price = price;
        this.reservation = reservation;
    }
}