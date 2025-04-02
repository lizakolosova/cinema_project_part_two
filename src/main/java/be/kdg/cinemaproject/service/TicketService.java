package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.Availability;
import be.kdg.cinemaproject.domain.Ticket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface TicketService {
    Ticket add(final double price, final LocalDateTime showtime, final String format, final Availability availability, final String image, final Long movieId, final Long cinemaId, final Long workerId);
    Ticket patch(final Long id, final double price, final LocalDateTime showtime, final Availability availability);
    List<Ticket> getAll();
    Ticket getById(final Long id);
    Optional<Ticket> getTicketByIdWithDetails(Long id);
    void  deleteById(Long id);
    Ticket existsById(Long id);
}
