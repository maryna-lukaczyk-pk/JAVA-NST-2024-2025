package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Movie;
import org.example.projectmanagerapp.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = getMovieById(id);
        movie.setTitle(movieDetails.getTitle());
        movie.setDescription(movieDetails.getDescription());
        movie.setDuration(movieDetails.getDuration());
        movie.setReleaseDate(movieDetails.getReleaseDate());
        movie.setGenre(movieDetails.getGenre());
        movie.setDirector(movieDetails.getDirector());
        movie.setAgeRestriction(movieDetails.getAgeRestriction());
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        Movie movie = getMovieById(id);
        movieRepository.delete(movie);
    }

    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }
}