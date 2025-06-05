package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Reservation;
import org.example.projectmanagerapp.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Operations for managing reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all reservations")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping
    @Operation(summary = "Create a new reservation")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservation,
            @RequestParam Long userId,
            @RequestParam Long screeningId) {
        Reservation created = reservationService.createReservation(reservation, userId, screeningId);
        URI location = URI.create("/api/reservations/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reservation by ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Reservation> getReservationById(
            @Parameter(description = "ID of reservation to retrieve") @PathVariable Long id) {
        try {
            Reservation reservation = reservationService.getReservationById(id);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get reservations by user ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Reservation> getReservationsByUser(
            @Parameter(description = "User ID to retrieve reservations for") @PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update reservation payment status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reservation> updateReservationStatus(
            @Parameter(description = "ID of reservation to update") @PathVariable Long id,
            @RequestParam boolean isPaid) {
        Reservation updated = reservationService.updateReservationStatus(id, isPaid);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reservation by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReservation(
            @Parameter(description = "ID of reservation to delete") @PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/screening/{screeningId}/available-seats")
    @Operation(summary = "Get available seats count for a screening")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Integer> getAvailableSeatsCount(
            @Parameter(description = "Screening ID") @PathVariable Long screeningId) {
        int availableSeats = reservationService.getAvailableSeatsCount(screeningId);
        return ResponseEntity.ok(availableSeats);
    }
}
