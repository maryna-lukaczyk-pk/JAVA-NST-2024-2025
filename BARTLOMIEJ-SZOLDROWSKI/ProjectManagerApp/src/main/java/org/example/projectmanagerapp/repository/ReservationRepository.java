package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Reservation;
import org.example.projectmanagerapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByIsPaid(boolean isPaid);
    long countByScreeningId(Long screeningId);
}