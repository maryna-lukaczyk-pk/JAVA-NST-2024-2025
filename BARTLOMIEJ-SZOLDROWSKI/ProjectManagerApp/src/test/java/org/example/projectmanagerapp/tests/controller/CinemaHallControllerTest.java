package org.example.projectmanagerapp.tests.controller;

import org.example.projectmanagerapp.controller.CinemaHallController;
import org.example.projectmanagerapp.entity.CinemaHall;
import org.example.projectmanagerapp.service.CinemaHallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CinemaHallControllerTest {

    private CinemaHallService cinemaHallService;
    private CinemaHallController cinemaHallController;

    private CinemaHall testHall;

    @BeforeEach
    void setUp() {
        // Tworzymy mock ręcznie, bez @MockBean
        cinemaHallService = Mockito.mock(CinemaHallService.class);
        // Wstrzykujemy mock do kontrolera przez konstruktor
        cinemaHallController = new CinemaHallController(cinemaHallService);

        testHall = new CinemaHall("Standard Hall", 100, false);
        testHall.setId(1L); // ustawiamy ID, bo w createCinemaHall zwracamy URI z ID
    }

    @Test
    @DisplayName("Should return all cinema halls")
    void getAllCinemaHalls_ShouldReturnAll() {
        List<CinemaHall> expected = List.of(testHall);
        when(cinemaHallService.getAllCinemaHalls()).thenReturn(expected);

        List<CinemaHall> result = cinemaHallController.getAllCinemaHalls();

        assertEquals(1, result.size());
        assertEquals("Standard Hall", result.get(0).getName());
        verify(cinemaHallService, times(1)).getAllCinemaHalls();
    }

    @Test
    @DisplayName("Should create cinema hall and return 201 with location")
    void createCinemaHall_ShouldReturnCreatedHall() {
        when(cinemaHallService.createCinemaHall(any(CinemaHall.class))).thenReturn(testHall);

        var response = cinemaHallController.createCinemaHall(testHall);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("/api/cinemahalls/1", response.getHeaders().getLocation().toString());
        assertEquals(testHall, response.getBody());
        verify(cinemaHallService, times(1)).createCinemaHall(testHall);
    }

    @Test
    @DisplayName("Should return cinema hall by ID")
    void getCinemaHallById_ShouldReturnHall() {
        when(cinemaHallService.getCinemaHallById(1L)).thenReturn(testHall);

        var response = cinemaHallController.getCinemaHallById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(testHall, response.getBody());
        verify(cinemaHallService, times(1)).getCinemaHallById(1L);
    }

    @Test
    @DisplayName("Should update cinema hall")
    void updateCinemaHall_ShouldReturnUpdated() {
        CinemaHall updated = new CinemaHall("Updated Hall", 150, true);
        updated.setId(1L);

        when(cinemaHallService.updateCinemaHall(eq(1L), any(CinemaHall.class))).thenReturn(updated);

        var response = cinemaHallController.updateCinemaHall(1L, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Hall", response.getBody().getName());
        verify(cinemaHallService, times(1)).updateCinemaHall(1L, updated);
    }

    @Test
    @DisplayName("Should delete cinema hall and return no content")
    void deleteCinemaHall_ShouldReturnNoContent() {
        doNothing().when(cinemaHallService).deleteCinemaHall(1L);

        var response = cinemaHallController.deleteCinemaHall(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(cinemaHallService, times(1)).deleteCinemaHall(1L);
    }

    @Test
    @DisplayName("Should return 3D halls")
    void get3DHalls_ShouldReturn3DHalls() {
        CinemaHall hall3D = new CinemaHall("3D Hall", 120, true);
        when(cinemaHallService.get3DHalls()).thenReturn(List.of(hall3D));

        List<CinemaHall> result = cinemaHallController.get3DHalls();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getHas3D());
        verify(cinemaHallService, times(1)).get3DHalls();
    }

    @Test
    @DisplayName("Should return halls with minimum capacity")
    void getHallsWithMinCapacity_ShouldReturnFiltered() {
        CinemaHall largeHall = new CinemaHall("Large Hall", 300, false);
        when(cinemaHallService.getHallsWithMinCapacity(200)).thenReturn(List.of(largeHall));

        List<CinemaHall> result = cinemaHallController.getHallsWithMinCapacity(200);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getCapacity() >= 200);
        verify(cinemaHallService, times(1)).getHallsWithMinCapacity(200);
    }
}
