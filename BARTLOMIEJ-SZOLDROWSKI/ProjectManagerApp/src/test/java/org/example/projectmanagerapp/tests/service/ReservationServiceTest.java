package org.example.projectmanagerapp.tests.service;

import org.example.projectmanagerapp.entity.*;
import org.example.projectmanagerapp.repository.*;
import org.example.projectmanagerapp.service.ReservationService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private ReservationService reservationService;

    private User testUser;
    private Screening testScreening;
    private Reservation testReservation;
    private CinemaHall testHall;

    @BeforeEach
    void setUp() {
        testUser = new User(
                "jan.kowalski",
                "securePassword123",
                "jan.kowalski@example.com",
                User.UserRole.USER
        );
        testHall = new CinemaHall("Hall 1", 100, true);

        Movie testMovie = new Movie(
                "Inception",
                "A thief who steals corporate secrets",
                148,
                LocalDate.of(2023, 6, 15),
                "Sci-Fi",
                "Christopher Nolan",
                12
        );

        LocalDateTime startTime = LocalDateTime.of(2023, 6, 20, 18, 0);
        LocalDateTime endTime = startTime.plusMinutes(testMovie.getDuration());

        testScreening = new Screening(
                startTime,
                endTime,
                25.99,  // cena biletu
                true,   // czy seans 3D
                testMovie,
                testHall
        );

        testReservation = new Reservation();
        testReservation.setUser(testUser);
        testReservation.setScreening(testScreening);
        testReservation.setIsPaid(false);
    }

    @Test
    @DisplayName("Should create reservation with linked user and screening")
    void createReservation_ShouldLinkUserAndScreening() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(testScreening));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        Reservation result = reservationService.createReservation(new Reservation(), 1L, 1L);

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals(testScreening, result.getScreening());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void createReservation_ShouldThrowWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                reservationService.createReservation(new Reservation(), 1L, 1L));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return all reservations")
    void getAllReservations_ShouldReturnAll() {
        when(reservationRepository.findAll()).thenReturn(List.of(testReservation));

        List<Reservation> result = reservationService.getAllReservations();

        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return reservation by ID")
    void getReservationById_ShouldReturnReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        Reservation result = reservationService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(testReservation, result);
    }

    @Test
    @DisplayName("Should throw exception when reservation not found")
    void getReservationById_ShouldThrowWhenNotFound() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                reservationService.getReservationById(1L));
    }

    @Test
    @DisplayName("Should return reservations by user")
    void getReservationsByUser_ShouldReturnUserReservations() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(reservationRepository.findByUser(testUser)).thenReturn(List.of(testReservation));

        List<Reservation> result = reservationService.getReservationsByUser(1L);

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0).getUser());
    }

    @Test
    @DisplayName("Should update reservation status")
    void updateReservationStatus_ShouldChangeStatus() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(testReservation)).thenReturn(testReservation);

        Reservation result = reservationService.updateReservationStatus(1L, true);

        assertTrue(result.getIsPaid());
        verify(reservationRepository, times(1)).save(testReservation);
    }

    @Test
    @DisplayName("Should delete reservation")
    void deleteReservation_ShouldCallDelete() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        doNothing().when(reservationRepository).delete(testReservation);

        reservationService.deleteReservation(1L);

        verify(reservationRepository, times(1)).delete(testReservation);
    }

    @Test
    @DisplayName("Should calculate available seats correctly")
    void getAvailableSeatsCount_ShouldCalculateCorrectly() {
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(testScreening));
        when(ticketRepository.countTicketsByScreeningId(1L)).thenReturn(30);

        int availableSeats = reservationService.getAvailableSeatsCount(1L);

        assertEquals(70, availableSeats); // 100 (capacity) - 30 (reserved)
    }

    @Test
    @DisplayName("Should throw exception when screening not found for seats calculation")
    void getAvailableSeatsCount_ShouldThrowWhenScreeningNotFound() {
        when(screeningRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                reservationService.getAvailableSeatsCount(1L));
    }
}