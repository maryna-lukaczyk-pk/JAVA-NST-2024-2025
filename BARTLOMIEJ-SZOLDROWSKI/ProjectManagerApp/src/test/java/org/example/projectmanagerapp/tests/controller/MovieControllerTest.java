package org.example.projectmanagerapp.tests.controller;

import org.example.projectmanagerapp.controller.MovieController;
import org.example.projectmanagerapp.entity.Movie;
import org.example.projectmanagerapp.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MovieControllerTest {

    private MovieService movieService;
    private MovieController movieController;

    private Movie testMovie;

    @BeforeEach
    void setUp() {
        movieService = Mockito.mock(MovieService.class);
        movieController = new MovieController(movieService);

        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTitle("Inception");
        testMovie.setGenre("Sci-Fi");
        // ustaw inne pola, jeśli masz
    }

    @Test
    @DisplayName("Get all movies should return list")
    void getAllMovies_shouldReturnList() {
        when(movieService.getAllMovies()).thenReturn(List.of(testMovie));

        List<Movie> result = movieController.getAllMovies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    @DisplayName("Create movie should return created movie with location")
    void createMovie_shouldReturnCreated() {
        when(movieService.createMovie(any(Movie.class))).thenReturn(testMovie);

        ResponseEntity<Movie> response = movieController.createMovie(testMovie);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("/api/movies/1", response.getHeaders().getLocation().toString());
        assertEquals(testMovie, response.getBody());
        verify(movieService, times(1)).createMovie(testMovie);
    }

    @Test
    @DisplayName("Update movie should return updated movie")
    void updateMovie_shouldReturnUpdated() {
        Movie updatedMovie = new Movie();
        updatedMovie.setId(1L);
        updatedMovie.setTitle("Interstellar");
        updatedMovie.setGenre("Sci-Fi");

        when(movieService.updateMovie(eq(1L), any(Movie.class))).thenReturn(updatedMovie);

        ResponseEntity<Movie> response = movieController.updateMovie(1L, updatedMovie);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Interstellar", response.getBody().getTitle());
        verify(movieService, times(1)).updateMovie(1L, updatedMovie);
    }

    @Test
    @DisplayName("Delete movie should return no content")
    void deleteMovie_shouldReturnNoContent() {
        doNothing().when(movieService).deleteMovie(1L);

        ResponseEntity<Void> response = movieController.deleteMovie(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(movieService, times(1)).deleteMovie(1L);
    }

    @Test
    @DisplayName("Get movie by ID should return movie")
    void getMovieById_shouldReturnMovie() {
        when(movieService.getMovieById(1L)).thenReturn(testMovie);

        ResponseEntity<Movie> response = movieController.getMovieById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testMovie, response.getBody());
        verify(movieService, times(1)).getMovieById(1L);
    }

    @Test
    @DisplayName("Get movie by ID should throw 404 if not found")
    void getMovieById_shouldThrowNotFound() {
        when(movieService.getMovieById(1L)).thenThrow(new RuntimeException("Movie not found"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            movieController.getMovieById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Movie not found"));
        verify(movieService, times(1)).getMovieById(1L);
    }

    @Test
    @DisplayName("Search movies by title should return list")
    void searchMoviesByTitle_shouldReturnList() {
        when(movieService.searchMoviesByTitle("Inception")).thenReturn(List.of(testMovie));

        List<Movie> result = movieController.searchMoviesByTitle("Inception");

        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        verify(movieService, times(1)).searchMoviesByTitle("Inception");
    }

    @Test
    @DisplayName("Get movies by genre should return list")
    void getMoviesByGenre_shouldReturnList() {
        when(movieService.getMoviesByGenre("Sci-Fi")).thenReturn(List.of(testMovie));

        List<Movie> result = movieController.getMoviesByGenre("Sci-Fi");

        assertEquals(1, result.size());
        assertEquals("Sci-Fi", result.get(0).getGenre());
        verify(movieService, times(1)).getMoviesByGenre("Sci-Fi");
    }
}

