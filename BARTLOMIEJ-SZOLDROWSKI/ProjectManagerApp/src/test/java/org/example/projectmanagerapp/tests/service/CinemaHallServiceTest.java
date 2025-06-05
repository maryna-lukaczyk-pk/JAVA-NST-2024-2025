package org.example.projectmanagerapp.tests.service;

import org.example.projectmanagerapp.entity.CinemaHall;
import org.example.projectmanagerapp.repository.CinemaHallRepository;
import org.example.projectmanagerapp.service.CinemaHallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CinemaHallServiceTest {

    @Mock
    private CinemaHallRepository cinemaHallRepository;

    @InjectMocks
    private CinemaHallService cinemaHallService;

    private CinemaHall testHall;
    private CinemaHall test3DHall;
    private CinemaHall testLargeHall;

    @BeforeEach
    void setUp() {
        testHall = new CinemaHall("Standard Hall", 100, false);
        test3DHall = new CinemaHall("3D Hall", 150, true);
        testLargeHall = new CinemaHall("Large Hall", 300, false);
    }

    @Test
    @DisplayName("Should successfully create cinema hall")
    void createCinemaHall_ShouldReturnSavedHall() {
        when(cinemaHallRepository.save(any(CinemaHall.class))).thenReturn(testHall);

        CinemaHall result = cinemaHallService.createCinemaHall(testHall);

        assertNotNull(result);
        assertEquals(testHall.getName(), result.getName());
        verify(cinemaHallRepository, times(1)).save(testHall);
    }

    @Test
    @DisplayName("Should return all cinema halls")
    void getAllCinemaHalls_ShouldReturnAllHalls() {
        List<CinemaHall> expectedHalls = Arrays.asList(testHall, test3DHall);
        when(cinemaHallRepository.findAll()).thenReturn(expectedHalls);

        List<CinemaHall> result = cinemaHallService.getAllCinemaHalls();

        assertEquals(2, result.size());
        verify(cinemaHallRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return cinema hall by ID")
    void getCinemaHallById_ShouldReturnHall() {
        when(cinemaHallRepository.findById(1L)).thenReturn(Optional.of(testHall));

        CinemaHall result = cinemaHallService.getCinemaHallById(1L);

        assertNotNull(result);
        assertEquals(testHall.getName(), result.getName());
    }

    @Test
    @DisplayName("Should throw exception when cinema hall not found")
    void getCinemaHallById_ShouldThrowException() {
        when(cinemaHallRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cinemaHallService.getCinemaHallById(1L));
        verify(cinemaHallRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return only 3D halls")
    void get3DHalls_ShouldReturn3DHalls() {
        when(cinemaHallRepository.findByHas3DTrue()).thenReturn(List.of(test3DHall));

        List<CinemaHall> result = cinemaHallService.get3DHalls();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getHas3D());
        verify(cinemaHallRepository, times(1)).findByHas3DTrue();
    }

    @Test
    @DisplayName("Should return halls with minimum capacity")
    void getHallsWithMinCapacity_ShouldReturnFilteredHalls() {
        when(cinemaHallRepository.findByCapacityGreaterThanEqual(200))
                .thenReturn(List.of(testLargeHall));

        List<CinemaHall> result = cinemaHallService.getHallsWithMinCapacity(200);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getCapacity() >= 200);
        verify(cinemaHallRepository, times(1)).findByCapacityGreaterThanEqual(200);
    }

    @Test
    @DisplayName("Should update cinema hall details")
    void updateCinemaHall_ShouldUpdateDetails() {
        CinemaHall existingHall = new CinemaHall("Old Hall", 100, false);
        CinemaHall updatedDetails = new CinemaHall("Updated Hall", 150, true);

        when(cinemaHallRepository.findById(1L)).thenReturn(Optional.of(existingHall));
        when(cinemaHallRepository.save(any(CinemaHall.class))).thenReturn(existingHall);

        CinemaHall result = cinemaHallService.updateCinemaHall(1L, updatedDetails);

        assertEquals(updatedDetails.getName(), result.getName());
        assertEquals(updatedDetails.getCapacity(), result.getCapacity());
        assertEquals(updatedDetails.getHas3D(), result.getHas3D());
        verify(cinemaHallRepository, times(1)).save(existingHall);
    }

    @Test
    @DisplayName("Should delete cinema hall")
    void deleteCinemaHall_ShouldCallDelete() {
        when(cinemaHallRepository.findById(1L)).thenReturn(Optional.of(testHall));
        doNothing().when(cinemaHallRepository).delete(testHall);

        cinemaHallService.deleteCinemaHall(1L);

        verify(cinemaHallRepository, times(1)).delete(testHall);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent hall")
    void deleteCinemaHall_ShouldThrowExceptionWhenNotFound() {
        when(cinemaHallRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cinemaHallService.deleteCinemaHall(1L));
        verify(cinemaHallRepository, never()).delete(any());
    }
}