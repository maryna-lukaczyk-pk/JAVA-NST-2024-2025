package org.example.projectmanagerapp.tests.service;

import org.example.projectmanagerapp.entity.*;
import org.example.projectmanagerapp.repository.*;
import org.example.projectmanagerapp.service.ScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private CinemaHallRepository cinemaHallRepository;

    @InjectMocks
    private ScreeningService screeningService;

    private Movie testMovie;
    private CinemaHall testHall;
    private Screening testScreening;

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

        testHall = new CinemaHall("Hall 1", 100, true);

        LocalDateTime startTime = LocalDateTime.of(2023, 6, 20, 18, 0);
        LocalDateTime endTime = startTime.plusMinutes(testMovie.getDuration());

        testScreening = new Screening(
                startTime,
                endTime,
                25.99,
                true,
                testMovie,
                testHall
        );
    }

    @Test
    @DisplayName("Should create screening with linked movie and hall")
    void createScreening_ShouldLinkMovieAndHall() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(cinemaHallRepository.findById(1L)).thenReturn(Optional.of(testHall));
        when(screeningRepository.save(any(Screening.class))).thenReturn(testScreening);

        Screening result = screeningService.createScreening(new Screening(), 1L, 1L);

        assertNotNull(result);
        assertEquals(testMovie, result.getMovie());
        assertEquals(testHall, result.getCinemaHall());
        verify(screeningRepository, times(1)).save(any(Screening.class));
    }

    @Test
    @DisplayName("Should throw exception when movie not found")
    void createScreening_ShouldThrowWhenMovieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                screeningService.createScreening(new Screening(), 1L, 1L));
        verify(screeningRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return all screenings")
    void getAllScreenings_ShouldReturnAll() {
        when(screeningRepository.findAll()).thenReturn(List.of(testScreening));

        List<Screening> result = screeningService.getAllScreenings();

        assertEquals(1, result.size());
        verify(screeningRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return screening by ID")
    void getScreeningById_ShouldReturnScreening() {
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(testScreening));

        Screening result = screeningService.getScreeningById(1L);

        assertNotNull(result);
        assertEquals(testScreening, result);
    }

    @Test
    @DisplayName("Should throw exception when screening not found")
    void getScreeningById_ShouldThrowWhenNotFound() {
        when(screeningRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                screeningService.getScreeningById(1L));
    }

    @Test
    @DisplayName("Should return screenings by movie")
    void getScreeningsByMovie_ShouldReturnFiltered() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(screeningRepository.findByMovie(testMovie)).thenReturn(List.of(testScreening));

        List<Screening> result = screeningService.getScreeningsByMovie(1L);

        assertEquals(1, result.size());
        assertEquals(testMovie, result.get(0).getMovie());
    }

    @Test
    @DisplayName("Should return screenings between dates")
    void getScreeningsBetweenDates_ShouldReturnFiltered() {
        LocalDateTime start = LocalDateTime.of(2023, 6, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 6, 30, 23, 59);

        when(screeningRepository.findByStartTimeBetween(start, end))
                .thenReturn(List.of(testScreening));

        List<Screening> result = screeningService.getScreeningsBetweenDates(start, end);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getStartTime().isAfter(start));
        assertTrue(result.get(0).getStartTime().isBefore(end));
    }

    @Test
    @DisplayName("Should delete screening")
    void deleteScreening_ShouldCallDelete() {
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(testScreening));
        doNothing().when(screeningRepository).delete(testScreening);

        screeningService.deleteScreening(1L);

        verify(screeningRepository, times(1)).delete(testScreening);
    }

    @Test
    @DisplayName("Should return true when hall is available")
    void isHallAvailable_ShouldReturnTrueWhenAvailable() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);

        when(screeningRepository.findScreeningsInHallBetweenTimes(1L, start, end))
                .thenReturn(List.of());

        assertTrue(screeningService.isHallAvailable(1L, start, end));
    }

    @Test
    @DisplayName("Should return false when hall is not available")
    void isHallAvailable_ShouldReturnFalseWhenOccupied() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(2);

        when(screeningRepository.findScreeningsInHallBetweenTimes(1L, start, end))
                .thenReturn(List.of(testScreening));

        assertFalse(screeningService.isHallAvailable(1L, start, end));
    }
}