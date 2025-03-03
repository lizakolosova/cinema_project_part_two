package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.Availability;
import be.kdg.cinemaproject.domain.Ticket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public interface TicketService {
    Ticket add(final double price, final LocalDateTime showtime, final String format, final Availability availability, final String image, final Long movieId, final Long cinemaId);
    Ticket patch(final Long id, final double price, final LocalDateTime showtime, final Availability availability);
    List<Ticket> getAll();
    Ticket getById(final Long id);
    Ticket getTicketByIdWithDetails(Long id);
}
