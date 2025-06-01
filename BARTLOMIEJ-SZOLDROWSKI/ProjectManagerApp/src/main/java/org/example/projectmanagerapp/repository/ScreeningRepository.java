package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Movie;
import org.example.projectmanagerapp.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByMovie(Movie movie);
    List<Screening> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Screening s WHERE s.cinemaHall.id = :hallId AND " +
            "((s.startTime BETWEEN :start AND :end) OR " +
            "(s.endTime BETWEEN :start AND :end))")
    List<Screening> findScreeningsInHallBetweenTimes(Long hallId, LocalDateTime start, LocalDateTime end);
}