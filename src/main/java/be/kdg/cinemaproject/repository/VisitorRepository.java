package be.kdg.cinemaproject.repository;

import be.kdg.cinemaproject.domain.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long>{
    Optional<Visitor> findByEmail(String email);

    Visitor getVisitorsByEmail(String email);
}
