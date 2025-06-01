package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.*;
import org.example.projectmanagerapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ScreeningRepository screeningRepository;
    private final TicketRepository ticketRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              UserRepository userRepository,
                              ScreeningRepository screeningRepository,
                              TicketRepository ticketRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.screeningRepository = screeningRepository;
        this.ticketRepository = ticketRepository;
    }

    public Reservation createReservation(Reservation reservation, Long userId, Long screeningId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("Screening not found"));

        reservation.setUser(user);
        reservation.setScreening(screening);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reservationRepository.findByUser(user);
    }

    public Reservation updateReservationStatus(Long id, boolean isPaid) {
        Reservation reservation = getReservationById(id);
        reservation.setIsPaid(isPaid);
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = getReservationById(id);
        reservationRepository.delete(reservation);
    }

    public int getAvailableSeatsCount(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("Screening not found"));
        int totalSeats = screening.getCinemaHall().getCapacity();
        int reservedSeats = ticketRepository.countTicketsByScreeningId(screeningId);
        return totalSeats - reservedSeats;
    }
}