package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Reservation;
import org.example.projectmanagerapp.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByReservation(Reservation reservation);
    List<Ticket> findByTicketType(String ticketType);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.reservation.screening.id = :screeningId")
    int countTicketsByScreeningId(Long screeningId);
}