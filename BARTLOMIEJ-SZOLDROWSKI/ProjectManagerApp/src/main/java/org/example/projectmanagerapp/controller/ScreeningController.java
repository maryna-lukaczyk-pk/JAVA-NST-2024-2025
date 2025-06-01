package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Screening;
import org.example.projectmanagerapp.service.ScreeningService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/screenings")
@Tag(name = "Screenings", description = "Operations for managing screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all screenings")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Screening> getAllScreenings() {
        return screeningService.getAllScreenings();
    }

    @PostMapping
    @Operation(summary = "Create a new screening")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Screening> createScreening(
            @RequestBody Screening screening,
            @RequestParam Long movieId,
            @RequestParam Long cinemaHallId) {
        Screening created = screeningService.createScreening(screening, movieId, cinemaHallId);
        URI location = URI.create("/api/screenings/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get screening by ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Screening> getScreeningById(
            @Parameter(description = "ID of screening to retrieve") @PathVariable Long id) {
        try {
            Screening screening = screeningService.getScreeningById(id);
            return ResponseEntity.ok(screening);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/movie/{movieId}")
    @Operation(summary = "Get screenings by movie ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Screening> getScreeningsByMovie(
            @Parameter(description = "Movie ID to filter screenings") @PathVariable Long movieId) {
        return screeningService.getScreeningsByMovie(movieId);
    }

    @GetMapping("/between")
    @Operation(summary = "Get screenings between start and end date/time")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Screening> getScreeningsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return screeningService.getScreeningsBetweenDates(start, end);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete screening by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteScreening(
            @Parameter(description = "ID of screening to delete") @PathVariable Long id) {
        screeningService.deleteScreening(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hall/{hallId}/availability")
    @Operation(summary = "Check if cinema hall is available between given times")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> isHallAvailable(
            @Parameter(description = "Cinema hall ID") @PathVariable Long hallId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        boolean available = screeningService.isHallAvailable(hallId, startTime, endTime);
        return ResponseEntity.ok(available);
    }
}
