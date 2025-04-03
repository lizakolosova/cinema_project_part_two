package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.*;
import be.kdg.cinemaproject.domain.exception.CinemaNotFoundException;
import be.kdg.cinemaproject.domain.exception.MovieNotFoundException;
import be.kdg.cinemaproject.domain.exception.TicketNotFoundException;
import be.kdg.cinemaproject.repository.CinemaRepository;
import be.kdg.cinemaproject.repository.MovieRepository;
import be.kdg.cinemaproject.repository.TicketRepository;
import be.kdg.cinemaproject.repository.VisitorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository;
    private final VisitorRepository visitorRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, CinemaRepository cinemaRepository, MovieRepository movieRepository, VisitorRepository visitorRepository) {
        this.ticketRepository = ticketRepository;
        this.cinemaRepository = cinemaRepository;
        this.movieRepository = movieRepository;
        this.visitorRepository = visitorRepository;
    }

    @Override
    public Ticket add(final double price, final LocalDateTime showtime, final String format, final Availability availability, final String image, final Long movieId, final Long cinemaId, final Long workerId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found", 404, "Not Found"));

        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new CinemaNotFoundException("Cinema not found", 404, "Not Found"));
        Visitor visitor = visitorRepository.findById(workerId).orElseThrow();
        final Ticket ticket = new Ticket();
        ticket.setPrice(price);
        ticket.setShowtime(showtime);
        ticket.setFormat(format);
        ticket.setAvailability(availability);
        ticket.setImage(image);
        ticket.setMovie(movie);
        ticket.setCinema(cinema);
        ticket.setVisitor(visitor);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getById(final Long id) {
        return ticketRepository.findTicketById(id).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
    }

    public List<Ticket> getAll() {
        return ticketRepository.findAllWithDetails();
    }

    @Override
    public Ticket patch(final Long id, final double price, final LocalDateTime showtime, final Availability availability) {
        final Ticket ticket = getById(id);
        if (price != 0) {
            ticket.setPrice(price);
        }
        if (showtime != null) {
            ticket.setShowtime(showtime);
        }
        if (availability != null) {
            ticket.setAvailability(availability);
        }
        return ticket;
    }

    public Optional<Ticket> getTicketByIdWithDetails(Long id) {
        return ticketRepository.findByIdWithDetails(id);
    }

    @Override
    public void deleteById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket with ID " + id + " not found for deletion."));
        Movie movie = ticket.getMovie();
        if (movie != null) {
            movie.getTickets().remove(ticket);
            movieRepository.save(movie);
        }
        ticketRepository.deleteById(id);
        log.info("Ticket with ID {} deleted successfully.", id);
    }

    @Override
    public Ticket existsById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }
}
