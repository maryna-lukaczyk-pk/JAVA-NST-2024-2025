package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByGenre(String genre);
    List<Movie> findByAgeRestrictionLessThanEqual(Integer maxAge);
}