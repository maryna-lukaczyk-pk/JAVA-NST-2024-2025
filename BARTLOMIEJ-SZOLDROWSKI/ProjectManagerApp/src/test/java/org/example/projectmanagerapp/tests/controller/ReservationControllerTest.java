package org.example.projectmanagerapp.tests.controller;

import org.example.projectmanagerapp.controller.ReservationController;
import org.example.projectmanagerapp.entity.Reservation;
import org.example.projectmanagerapp.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    private ReservationService reservationService;
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        reservationService = mock(ReservationService.class);
        reservationController = new ReservationController(reservationService);
    }

    @Test
    @DisplayName("Should return all reservations for admin")
    void getAllReservations_returnsList() {
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationService.getAllReservations()).thenReturn(reservations);

        List<Reservation> result = reservationController.getAllReservations();

        assertEquals(2, result.size());
        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    @DisplayName("Should create a new reservation")
    void createReservation_createsAndReturnsReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(10L);
        when(reservationService.createReservation(any(Reservation.class), eq(1L), eq(2L)))
                .thenReturn(reservation);

        ResponseEntity<Reservation> response = reservationController.createReservation(reservation, 1L, 2L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/api/reservations/10"), response.getHeaders().getLocation());
        assertEquals(10L, response.getBody().getId());
        verify(reservationService, times(1)).createReservation(reservation, 1L, 2L);
    }

    @Test
    @DisplayName("Should return reservation by id")
    void getReservationById_returnsReservation() {
        Reservation reservation = new Reservation();
        reservation.setId(5L);
        when(reservationService.getReservationById(5L)).thenReturn(reservation);

        ResponseEntity<Reservation> response = reservationController.getReservationById(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody().getId());
        verify(reservationService, times(1)).getReservationById(5L);
    }

    @Test
    @DisplayName("Should throw 404 if reservation not found")
    void getReservationById_notFound_throwsException() {
        when(reservationService.getReservationById(99L)).thenThrow(new RuntimeException("Not found"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            reservationController.getReservationById(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Not found"));
        verify(reservationService, times(1)).getReservationById(99L);
    }

    @Test
    @DisplayName("Should return reservations by user id")
    void getReservationsByUser_returnsList() {
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationService.getReservationsByUser(7L)).thenReturn(reservations);

        List<Reservation> result = reservationController.getReservationsByUser(7L);

        assertEquals(2, result.size());
        verify(reservationService, times(1)).getReservationsByUser(7L);
    }

    @Test
    @DisplayName("Should update reservation status")
    void updateReservationStatus_returnsUpdated() {
        Reservation updated = new Reservation();
        updated.setId(3L);
        when(reservationService.updateReservationStatus(3L, true)).thenReturn(updated);

        ResponseEntity<Reservation> response = reservationController.updateReservationStatus(3L, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3L, response.getBody().getId());
        verify(reservationService, times(1)).updateReservationStatus(3L, true);
    }

    @Test
    @DisplayName("Should delete reservation")
    void deleteReservation_deletesSuccessfully() {
        doNothing().when(reservationService).deleteReservation(4L);

        ResponseEntity<Void> response = reservationController.deleteReservation(4L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reservationService, times(1)).deleteReservation(4L);
    }

    @Test
    @DisplayName("Should get available seats count")
    void getAvailableSeatsCount_returnsCount() {
        when(reservationService.getAvailableSeatsCount(8L)).thenReturn(42);

        ResponseEntity<Integer> response = reservationController.getAvailableSeatsCount(8L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(42, response.getBody());
        verify(reservationService, times(1)).getAvailableSeatsCount(8L);
    }
}

