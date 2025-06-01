package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Ticket;
import org.example.projectmanagerapp.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Tickets", description = "Operations for managing tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @Operation(summary = "Create a new ticket")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ticket> createTicket(
            @RequestBody Ticket ticket,
            @RequestParam Long reservationId) {
        Ticket created = ticketService.createTicket(ticket, reservationId);
        URI location = URI.create("/api/tickets/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/reservation/{reservationId}")
    @Operation(summary = "Get tickets by reservation ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Ticket> getTicketsByReservation(
            @Parameter(description = "Reservation ID") @PathVariable Long reservationId) {
        return ticketService.getTicketsByReservation(reservationId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Ticket> getTicketById(
            @Parameter(description = "Ticket ID") @PathVariable Long id) {
        try {
            Ticket ticket = ticketService.getTicketById(id);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ticket by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTicket(
            @Parameter(description = "Ticket ID") @PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservation/{reservationId}/totalPrice")
    @Operation(summary = "Calculate total price for a reservation")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Double> calculateTotalPriceForReservation(
            @Parameter(description = "Reservation ID") @PathVariable Long reservationId) {
        double totalPrice = ticketService.calculateTotalPriceForReservation(reservationId);
        return ResponseEntity.ok(totalPrice);
    }
}
