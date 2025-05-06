package be.kdg.cinemaproject.repository;

import be.kdg.cinemaproject.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findTicketById(@Param("id") Long id);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.movie JOIN FETCH t.cinema JOIN FETCH t.visitor")
    List<Ticket> findAllWithDetails();

    @Query("SELECT t FROM Ticket t JOIN FETCH t.movie JOIN FETCH t.cinema JOIN FETCH t.visitor WHERE t.id = :id")
    Optional<Ticket> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.visitor WHERE t.id = :id")
    Optional<Ticket> findByIdWithVisitor(@Param("id") Long id);
}
