package org.example.projectmanagerapp.tests.service;

import org.example.projectmanagerapp.entity.Movie;
import org.example.projectmanagerapp.repository.MovieRepository;
import org.example.projectmanagerapp.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie testMovie;
    private Movie testMovie2;

    @BeforeEach
    void setUp() {
        testMovie = new Movie(
                "Inception",
                "A thief who steals corporate secrets",
                148,
                LocalDate.of(2023, 6, 15),
                "Sci-Fi",
                "Christopher Nolan",
                12
        );

        testMovie2 = new Movie(
                "The Shawshank Redemption",
                "Two imprisoned men bond over a number of years",
                142,
                LocalDate.of(1994, 9, 23),
                "Drama",
                "Frank Darabont",
                16
        );
    }

    @Test
    @DisplayName("Should successfully create movie")
    void createMovie_ShouldReturnSavedMovie() {
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie);

        Movie result = movieService.createMovie(testMovie);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        assertEquals("Sci-Fi", result.getGenre());
        verify(movieRepository, times(1)).save(testMovie);
    }

    @Test
    @DisplayName("Should return all movies")
    void getAllMovies_ShouldReturnAllMovies() {
        when(movieRepository.findAll()).thenReturn(List.of(testMovie, testMovie2));

        List<Movie> result = movieService.getAllMovies();

        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return movie by ID")
    void getMovieById_ShouldReturnMovie() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));

        Movie result = movieService.getMovieById(1L);

        assertNotNull(result);
        assertEquals(testMovie.getTitle(), result.getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when movie not found")
    void getMovieById_ShouldThrowException() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> movieService.getMovieById(1L));
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should update all movie fields")
    void updateMovie_ShouldUpdateAllFields() {
        Movie updatedDetails = new Movie(
                "Inception 2",
                "New description",
                150,
                LocalDate.of(2024, 1, 1),
                "Sci-Fi Thriller",
                "Christopher Nolan",
                14
        );

        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie);

        Movie result = movieService.updateMovie(1L, updatedDetails);

        assertEquals(updatedDetails.getTitle(), result.getTitle());
        assertEquals(updatedDetails.getDescription(), result.getDescription());
        assertEquals(updatedDetails.getDuration(), result.getDuration());
        assertEquals(updatedDetails.getReleaseDate(), result.getReleaseDate());
        assertEquals(updatedDetails.getGenre(), result.getGenre());
        assertEquals(updatedDetails.getDirector(), result.getDirector());
        assertEquals(updatedDetails.getAgeRestriction(), result.getAgeRestriction());
        verify(movieRepository, times(1)).save(testMovie);
    }

    @Test
    @DisplayName("Should delete movie")
    void deleteMovie_ShouldCallDelete() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        doNothing().when(movieRepository).delete(testMovie);

        movieService.deleteMovie(1L);

        verify(movieRepository, times(1)).delete(testMovie);
    }

    @Test
    @DisplayName("Should search movies by title (case insensitive)")
    void searchMoviesByTitle_ShouldReturnMatchingMovies() {
        String searchTerm = "inception";
        when(movieRepository.findByTitleContainingIgnoreCase(searchTerm))
                .thenReturn(List.of(testMovie));

        List<Movie> result = movieService.searchMoviesByTitle(searchTerm);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getTitle().contains("Inception"));
        verify(movieRepository, times(1)).findByTitleContainingIgnoreCase(searchTerm);
    }

    @Test
    @DisplayName("Should return movies by genre")
    void getMoviesByGenre_ShouldReturnFilteredMovies() {
        when(movieRepository.findByGenre("Sci-Fi")).thenReturn(List.of(testMovie));

        List<Movie> result = movieService.getMoviesByGenre("Sci-Fi");

        assertEquals(1, result.size());
        assertEquals("Sci-Fi", result.get(0).getGenre());
        verify(movieRepository, times(1)).findByGenre("Sci-Fi");
    }

    @Test
    @DisplayName("Should return empty list when no movies match genre")
    void getMoviesByGenre_ShouldReturnEmptyListWhenNoMatches() {
        when(movieRepository.findByGenre(anyString())).thenReturn(List.of());

        List<Movie> result = movieService.getMoviesByGenre("Horror");

        assertTrue(result.isEmpty());
        verify(movieRepository, times(1)).findByGenre("Horror");
    }
}