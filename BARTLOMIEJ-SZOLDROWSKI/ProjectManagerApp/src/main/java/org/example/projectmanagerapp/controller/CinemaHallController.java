package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.CinemaHall;
import org.example.projectmanagerapp.service.CinemaHallService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cinemahalls")
@Tag(name = "Cinema Halls", description = "Operations for managing cinema halls")
public class CinemaHallController {

    private final CinemaHallService cinemaHallService;

    public CinemaHallController(CinemaHallService cinemaHallService) {
        this.cinemaHallService = cinemaHallService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all cinema halls")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CinemaHall> getAllCinemaHalls() {
        return cinemaHallService.getAllCinemaHalls();
    }

    @PostMapping
    @Operation(summary = "Create a new cinema hall")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaHall> createCinemaHall(@RequestBody CinemaHall cinemaHall) {
        CinemaHall created = cinemaHallService.createCinemaHall(cinemaHall);
        URI location = URI.create("/api/cinemahalls/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cinema hall by ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CinemaHall> getCinemaHallById(@PathVariable Long id) {
        CinemaHall hall = cinemaHallService.getCinemaHallById(id);
        return ResponseEntity.ok(hall);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing cinema hall")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CinemaHall> updateCinemaHall(@PathVariable Long id,
                                                       @RequestBody CinemaHall hallDetails) {
        CinemaHall updated = cinemaHallService.updateCinemaHall(id, hallDetails);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete cinema hall by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCinemaHall(@PathVariable Long id) {
        cinemaHallService.deleteCinemaHall(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/3d")
    @Operation(summary = "Get all cinema halls with 3D support")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CinemaHall> get3DHalls() {
        return cinemaHallService.get3DHalls();
    }

    @GetMapping("/capacity")
    @Operation(summary = "Get cinema halls with minimum capacity")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CinemaHall> getHallsWithMinCapacity(@RequestParam int minCapacity) {
        return cinemaHallService.getHallsWithMinCapacity(minCapacity);
    }
}
