package be.kdg.cinemaproject.repository;

import be.kdg.cinemaproject.domain.Worker;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long>{
    Optional<Worker> findByEmail(String email);
}
