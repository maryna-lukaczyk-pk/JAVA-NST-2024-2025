package org.example.projectmanagerapp.tests.controller;

import org.example.projectmanagerapp.controller.TicketController;
import org.example.projectmanagerapp.entity.Ticket;
import org.example.projectmanagerapp.service.TicketService;
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

class TicketControllerTest {

    private TicketService ticketService;
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        ticketService = mock(TicketService.class);
        ticketController = new TicketController(ticketService);
    }

    @Test
    @DisplayName("Should create ticket and return response with location")
    void createTicket_createsAndReturns() {
        Ticket ticket = new Ticket();
        ticket.setId(123L);
        when(ticketService.createTicket(ticket, 10L)).thenReturn(ticket);

        ResponseEntity<Ticket> response = ticketController.createTicket(ticket, 10L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/api/tickets/123"), response.getHeaders().getLocation());
        assertEquals(123L, response.getBody().getId());
        verify(ticketService, times(1)).createTicket(ticket, 10L);
    }

    @Test
    @DisplayName("Should get tickets by reservation ID")
    void getTicketsByReservation_returnsList() {
        List<Ticket> tickets = List.of(new Ticket(), new Ticket());
        when(ticketService.getTicketsByReservation(5L)).thenReturn(tickets);

        List<Ticket> result = ticketController.getTicketsByReservation(5L);

        assertEquals(2, result.size());
        verify(ticketService, times(1)).getTicketsByReservation(5L);
    }

    @Test
    @DisplayName("Should get ticket by ID")
    void getTicketById_returnsTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(50L);
        when(ticketService.getTicketById(50L)).thenReturn(ticket);

        ResponseEntity<Ticket> response = ticketController.getTicketById(50L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(50L, response.getBody().getId());
        verify(ticketService, times(1)).getTicketById(50L);
    }

    @Test
    @DisplayName("Should throw NOT_FOUND when ticket not found")
    void getTicketById_notFound_throwsException() {
        when(ticketService.getTicketById(999L)).thenThrow(new RuntimeException("Ticket not found"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            ticketController.getTicketById(999L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Ticket not found"));
        verify(ticketService, times(1)).getTicketById(999L);
    }

    @Test
    @DisplayName("Should delete ticket")
    void deleteTicket_deletesSuccessfully() {
        doNothing().when(ticketService).deleteTicket(77L);

        ResponseEntity<Void> response = ticketController.deleteTicket(77L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ticketService, times(1)).deleteTicket(77L);
    }

    @Test
    @DisplayName("Should calculate total price for reservation")
    void calculateTotalPriceForReservation_returnsTotal() {
        when(ticketService.calculateTotalPriceForReservation(33L)).thenReturn(150.0);

        ResponseEntity<Double> response = ticketController.calculateTotalPriceForReservation(33L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(150.0, response.getBody());
        verify(ticketService, times(1)).calculateTotalPriceForReservation(33L);
    }
}

