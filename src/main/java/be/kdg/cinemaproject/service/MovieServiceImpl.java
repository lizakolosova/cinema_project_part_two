package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.*;
import be.kdg.cinemaproject.domain.exception.MovieNotFoundException;
import be.kdg.cinemaproject.controller.converter.StringToGenreConverter;
import be.kdg.cinemaproject.repository.MovieRepository;
import be.kdg.cinemaproject.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final TicketRepository ticketRepository;
    private final StringToGenreConverter stringToGenreConverter;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, TicketRepository ticketRepository, StringToGenreConverter stringToGenreConverter) {
        this.movieRepository = movieRepository;
        this.ticketRepository = ticketRepository;
        this.stringToGenreConverter = stringToGenreConverter;
    }

    @Transactional
    @Override
    public void addMovie(Movie movie) {
        log.debug("Attempting to save movie: {}", movie.getTitle());
            movieRepository.save(movie);
            log.info("Movie '{}' saved successfully.", movie.getTitle());
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> getMoviesByGenreAndRating(Genre genre, Double rating) {
        return movieRepository.findMoviesByGenreAndRating(genre, rating);
    }

    @Override
    public List<Movie> getFilteredMovies(String genreInput, Double rating) {
        Genre genre = null;
        if (genreInput != null && !genreInput.isEmpty()) {
            genre = stringToGenreConverter.convert(genreInput);
        }

        return getMoviesByGenreAndRating(genre, rating);
    }

    @Override
    public void deleteById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with ID " + id + " not found for deletion.", 404 , "Not found"));

        for (Ticket ticket: new ArrayList<>(movie.getTickets())) {
            ticket.setMovie(null);
            ticketRepository.save(ticket);
        }

        movieRepository.deleteById(id);
        log.info("Movie with ID {} deleted successfully.", id);
    }


    @Override
    public Movie findByIdWithCinemas(Long id) {
        Movie movie = movieRepository.findByIdWithSessionsAndCinemas(id);
        if (movie == null) {
            throw new MovieNotFoundException("Movie not found. Id:" + id, 404, "Not found");
        }
        return movie;
    }

    @Override
    public List<Movie> findByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public Movie existsById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }
}
