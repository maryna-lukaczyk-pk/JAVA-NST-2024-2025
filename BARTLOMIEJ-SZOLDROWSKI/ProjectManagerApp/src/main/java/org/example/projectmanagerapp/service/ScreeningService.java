package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.*;
import org.example.projectmanagerapp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ScreeningService {
    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final CinemaHallRepository cinemaHallRepository;

    public ScreeningService(ScreeningRepository screeningRepository,
                            MovieRepository movieRepository,
                            CinemaHallRepository cinemaHallRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.cinemaHallRepository = cinemaHallRepository;
    }

    public Screening createScreening(Screening screening, Long movieId, Long cinemaHallId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        CinemaHall cinemaHall = cinemaHallRepository.findById(cinemaHallId)
                .orElseThrow(() -> new RuntimeException("Cinema hall not found"));

        screening.setMovie(movie);
        screening.setCinemaHall(cinemaHall);
        return screeningRepository.save(screening);
    }

    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    public Screening getScreeningById(Long id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screening not found with id: " + id));
    }

    public List<Screening> getScreeningsByMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return screeningRepository.findByMovie(movie);
    }

    public List<Screening> getScreeningsBetweenDates(LocalDateTime start, LocalDateTime end) {
        return screeningRepository.findByStartTimeBetween(start, end);
    }

    public void deleteScreening(Long id) {
        Screening screening = getScreeningById(id);
        screeningRepository.delete(screening);
    }

    public boolean isHallAvailable(Long hallId, LocalDateTime startTime, LocalDateTime endTime) {
        return screeningRepository.findScreeningsInHallBetweenTimes(hallId, startTime, endTime).isEmpty();
    }
}