package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.CinemaHall;
import org.example.projectmanagerapp.repository.CinemaHallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CinemaHallService {
    private final CinemaHallRepository cinemaHallRepository;

    public CinemaHallService(CinemaHallRepository cinemaHallRepository) {
        this.cinemaHallRepository = cinemaHallRepository;
    }

    public CinemaHall createCinemaHall(CinemaHall cinemaHall) {
        return cinemaHallRepository.save(cinemaHall);
    }

    public List<CinemaHall> getAllCinemaHalls() {
        return cinemaHallRepository.findAll();
    }

    public CinemaHall getCinemaHallById(Long id) {
        return cinemaHallRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema hall not found with id: " + id));
    }

    public List<CinemaHall> get3DHalls() {
        return cinemaHallRepository.findByHas3DTrue();
    }

    public List<CinemaHall> getHallsWithMinCapacity(int minCapacity) {
        return cinemaHallRepository.findByCapacityGreaterThanEqual(minCapacity);
    }

    public CinemaHall updateCinemaHall(Long id, CinemaHall hallDetails) {
        CinemaHall hall = getCinemaHallById(id);
        hall.setName(hallDetails.getName());
        hall.setCapacity(hallDetails.getCapacity());
        hall.setHas3D(hallDetails.getHas3D());
        return cinemaHallRepository.save(hall);
    }

    public void deleteCinemaHall(Long id) {
        CinemaHall hall = getCinemaHallById(id);
        cinemaHallRepository.delete(hall);
    }
}