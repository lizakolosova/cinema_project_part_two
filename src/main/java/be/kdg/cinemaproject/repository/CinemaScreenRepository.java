package be.kdg.cinemaproject.repository;

import be.kdg.cinemaproject.domain.CinemaScreen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaScreenRepository extends JpaRepository<CinemaScreen, Long> {
}

