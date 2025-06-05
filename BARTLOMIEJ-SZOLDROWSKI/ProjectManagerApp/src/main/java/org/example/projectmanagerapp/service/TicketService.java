package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.*;
import org.example.projectmanagerapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    public TicketService(TicketRepository ticketRepository,
                         ReservationRepository reservationRepository) {
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
    }

    public Ticket createTicket(Ticket ticket, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        ticket.setReservation(reservation);
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsByReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return ticketRepository.findByReservation(reservation);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }

    public void deleteTicket(Long id) {
        Ticket ticket = getTicketById(id);
        ticketRepository.delete(ticket);
    }

    public double calculateTotalPriceForReservation(Long reservationId) {
        List<Ticket> tickets = getTicketsByReservation(reservationId);
        return tickets.stream().mapToDouble(Ticket::getPrice).sum();
    }
}