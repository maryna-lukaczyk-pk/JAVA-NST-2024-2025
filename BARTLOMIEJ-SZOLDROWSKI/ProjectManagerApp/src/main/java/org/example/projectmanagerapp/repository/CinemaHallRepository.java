package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaHallRepository extends JpaRepository<CinemaHall, Long> {
    List<CinemaHall> findByHas3DTrue();
    List<CinemaHall> findByCapacityGreaterThanEqual(int minCapacity);
}