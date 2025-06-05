package org.example.projectmanagerapp.tests.controller;

import org.example.projectmanagerapp.controller.ScreeningController;
import org.example.projectmanagerapp.entity.Screening;
import org.example.projectmanagerapp.service.ScreeningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScreeningControllerTest {

    private ScreeningService screeningService;
    private ScreeningController screeningController;

    @BeforeEach
    void setUp() {
        screeningService = mock(ScreeningService.class);
        screeningController = new ScreeningController(screeningService);
    }

    @Test
    @DisplayName("Should return all screenings")
    void getAllScreenings_returnsList() {
        List<Screening> screenings = List.of(new Screening(), new Screening());
        when(screeningService.getAllScreenings()).thenReturn(screenings);

        List<Screening> result = screeningController.getAllScreenings();

        assertEquals(2, result.size());
        verify(screeningService, times(1)).getAllScreenings();
    }

    @Test
    @DisplayName("Should create screening and return response with location")
    void createScreening_createsAndReturns() {
        Screening screening = new Screening();
        screening.setId(100L);
        when(screeningService.createScreening(any(Screening.class), eq(1L), eq(2L))).thenReturn(screening);

        ResponseEntity<Screening> response = screeningController.createScreening(screening, 1L, 2L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/api/screenings/100"), response.getHeaders().getLocation());
        assertEquals(100L, response.getBody().getId());
        verify(screeningService, times(1)).createScreening(screening, 1L, 2L);
    }

    @Test
    @DisplayName("Should return screening by id")
    void getScreeningById_returnsScreening() {
        Screening screening = new Screening();
        screening.setId(5L);
        when(screeningService.getScreeningById(5L)).thenReturn(screening);

        ResponseEntity<Screening> response = screeningController.getScreeningById(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody().getId());
        verify(screeningService, times(1)).getScreeningById(5L);
    }

    @Test
    @DisplayName("Should throw NOT_FOUND when screening not found")
    void getScreeningById_notFound_throwsException() {
        when(screeningService.getScreeningById(99L)).thenThrow(new RuntimeException("Screening not found"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            screeningController.getScreeningById(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Screening not found"));
        verify(screeningService, times(1)).getScreeningById(99L);
    }

    @Test
    @DisplayName("Should return screenings by movie id")
    void getScreeningsByMovie_returnsList() {
        List<Screening> screenings = List.of(new Screening(), new Screening());
        when(screeningService.getScreeningsByMovie(10L)).thenReturn(screenings);

        List<Screening> result = screeningController.getScreeningsByMovie(10L);

        assertEquals(2, result.size());
        verify(screeningService, times(1)).getScreeningsByMovie(10L);
    }

    @Test
    @DisplayName("Should return screenings between dates")
    void getScreeningsBetweenDates_returnsList() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusDays(1);
        List<Screening> screenings = List.of(new Screening(), new Screening());
        when(screeningService.getScreeningsBetweenDates(start, end)).thenReturn(screenings);

        List<Screening> result = screeningController.getScreeningsBetweenDates(start, end);

        assertEquals(2, result.size());
        verify(screeningService, times(1)).getScreeningsBetweenDates(start, end);
    }

    @Test
    @DisplayName("Should delete screening successfully")
    void deleteScreening_deletesSuccessfully() {
        doNothing().when(screeningService).deleteScreening(7L);

        ResponseEntity<Void> response = screeningController.deleteScreening(7L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(screeningService, times(1)).deleteScreening(7L);
    }

    @Test
    @DisplayName("Should return hall availability")
    void isHallAvailable_returnsBoolean() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);
        when(screeningService.isHallAvailable(3L, startTime, endTime)).thenReturn(true);

        ResponseEntity<Boolean> response = screeningController.isHallAvailable(3L, startTime, endTime);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(screeningService, times(1)).isHallAvailable(3L, startTime, endTime);
    }
}

