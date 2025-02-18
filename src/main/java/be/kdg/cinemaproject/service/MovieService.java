package be.kdg.cinemaproject.service;


import be.kdg.cinemaproject.domain.Genre;
import be.kdg.cinemaproject.domain.Movie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public interface MovieService {
    List<Movie> getAllMovies();
    List<Movie> getMoviesByGenreAndRating(Genre genre, Double rating);
    void addMovie(Movie movie);
    List<Movie> getFilteredMovies(String genreInput, Double rating);
    Movie findByIdWithCinemas(Long id);
    void  deleteById(Long id);
    List<Movie> findByTitle(String title);
    Movie existsById(Long id);
}