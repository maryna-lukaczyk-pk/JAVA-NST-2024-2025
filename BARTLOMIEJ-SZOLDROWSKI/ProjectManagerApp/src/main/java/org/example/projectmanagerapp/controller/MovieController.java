package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Movie;
import org.example.projectmanagerapp.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movies", description = "Operations for managing movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // GET: Retrieve all movies
    @GetMapping
    @Operation(summary = "Retrieve all movies")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    // POST: Create new movie
    @PostMapping
    @Operation(summary = "Create a new movie")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie created = movieService.createMovie(movie);
        URI location = URI.create("/api/movies/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    // PUT: Update movie
    @PutMapping("/{id}")
    @Operation(summary = "Update existing movie")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> updateMovie(
            @Parameter(description = "ID of movie to update") @PathVariable Long id,
            @RequestBody Movie movieDetails) {
        Movie updated = movieService.updateMovie(id, movieDetails);
        return ResponseEntity.ok(updated);
    }

    // DELETE: Delete movie
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete movie by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "ID of movie to delete") @PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    // GET: Get movie by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get movie by ID")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Movie> getMovieById(
            @Parameter(description = "ID of movie to retrieve") @PathVariable Long id) {
        try {
            Movie movie = movieService.getMovieById(id);
            return ResponseEntity.ok(movie);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // GET: Search movies by title
    @GetMapping("/search")
    @Operation(summary = "Search movies by title")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Movie> searchMoviesByTitle(@RequestParam String title) {
        return movieService.searchMoviesByTitle(title);
    }

    // GET: Get movies by genre
    @GetMapping("/genre/{genre}")
    @Operation(summary = "Get movies by genre")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Movie> getMoviesByGenre(@PathVariable String genre) {
        return movieService.getMoviesByGenre(genre);
    }
}
