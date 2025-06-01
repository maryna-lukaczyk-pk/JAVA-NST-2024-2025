package org.example.projectmanagerapp.tests.service;

import org.example.projectmanagerapp.entity.*;
import org.example.projectmanagerapp.repository.*;
import org.example.projectmanagerapp.service.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("createTicket: should link ticket with reservation and save")
    void createTicket_ShouldLinkReservationAndSave() {
        // given
        Long reservationId = 1L;
        Ticket ticket = new Ticket();
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        // when
        Ticket result = ticketService.createTicket(ticket, reservationId);

        // then
        assertNotNull(result);
        assertEquals(reservation, result.getReservation());
        verify(reservationRepository).findById(reservationId);
        verify(ticketRepository).save(ticket);
    }

    @Test
    @DisplayName("createTicket: should throw exception if reservation not found")
    void createTicket_ShouldThrowIfReservationNotFound() {
        // given
        Long reservationId = 1L;
        Ticket ticket = new Ticket();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ticketService.createTicket(ticket, reservationId));

        assertEquals("Reservation not found", exception.getMessage());
        verify(reservationRepository).findById(reservationId);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    @DisplayName("calculateTotalPrice: should sum prices of all tickets")
    void calculateTotalPrice_ShouldSumTicketPrices() {
        // given
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        Ticket ticket1 = new Ticket();
        ticket1.setPrice(20.0);
        Ticket ticket2 = new Ticket();
        ticket2.setPrice(30.0);
        List<Ticket> tickets = List.of(ticket1, ticket2);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(ticketRepository.findByReservation(reservation)).thenReturn(tickets);

        // when
        double total = ticketService.calculateTotalPriceForReservation(reservationId);

        // then
        assertEquals(50.0, total);
        verify(ticketRepository).findByReservation(reservation);
    }

    @Test
    @DisplayName("calculateTotalPrice: should throw if reservation not found")
    void calculateTotalPrice_ShouldThrowIfReservationNotFound() {
        // given
        Long reservationId = 1L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ticketService.calculateTotalPriceForReservation(reservationId));

        assertEquals("Reservation not found", exception.getMessage());
        verify(ticketRepository, never()).findByReservation(any());
    }
}
