package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.Movie;
import org.springframework.stereotype.Service;
import be.kdg.cinemaproject.domain.Cinema;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface CinemaService {
    List<Cinema> getAllCinemas();
    List<Cinema> getCinemasByCapacity(int minCapacity);
    void saveCinema(Cinema cinema);
    Cinema findByIdWithMovies(Long id);
    void deleteById(Long id);
    Cinema existsById(Long id);
}
