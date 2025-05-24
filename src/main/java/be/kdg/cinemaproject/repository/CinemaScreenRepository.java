package be.kdg.cinemaproject.repository;

import be.kdg.cinemaproject.domain.CinemaScreen;
import be.kdg.cinemaproject.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaScreenRepository extends JpaRepository<CinemaScreen, Long> {
    List<CinemaScreen> findByScreenTypeContainingIgnoreCase(String screenType);
}

