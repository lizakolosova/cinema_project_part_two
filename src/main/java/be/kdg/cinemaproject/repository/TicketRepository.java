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

    @Query("SELECT COUNT(t) > 0 FROM Ticket t WHERE t.movie.id = :movieId AND t.visitor.id = :visitorId")
    boolean isVisitorAssignedToMovie(@Param("movieId") Long movieId, @Param("visitorId") Long visitorId);

    @Query("SELECT COUNT(t) > 0 FROM Ticket t WHERE t.cinema.id = :cinemaId AND t.visitor.id = :visitorId")
    boolean isVisitorAssignedToCinema(@Param("cinemaId") Long cinemaId, @Param("visitorId") Long visitorId);
}
