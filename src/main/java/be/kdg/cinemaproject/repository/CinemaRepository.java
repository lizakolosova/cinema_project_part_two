package be.kdg.cinemaproject.repository;

import be.kdg.cinemaproject.domain.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Optional<Cinema> findByName(String name);

    List<Cinema> findByAddress(String address);

    List<Cinema> findCinemaByCapacityAfter(int capacity);

    @Query("""
    FROM Cinema c
    LEFT JOIN FETCH c.screens cs
    WHERE c.id = :id
""")
    Cinema findByIdWithScreens(@Param("id") Long id);
}