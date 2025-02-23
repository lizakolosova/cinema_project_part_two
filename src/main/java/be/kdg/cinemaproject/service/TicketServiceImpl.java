package be.kdg.cinemaproject.service;

import be.kdg.cinemaproject.domain.Availability;
import be.kdg.cinemaproject.domain.Ticket;
import be.kdg.cinemaproject.domain.exception.TicketNotFoundException;
import be.kdg.cinemaproject.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket add(final double price, final LocalDateTime showtime, final String format, final Availability availability, final String image) {
        final Ticket ticket = new Ticket();
        ticket.setPrice(price);
        ticket.setShowtime(showtime);
        ticket.setFormat(format);
        ticket.setAvailability(availability);
        ticket.setImage(image);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getById(final Long id) {
        return ticketRepository.findTicketById(id).orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
    }

    public List<Ticket> getAll() {
        return ticketRepository.findAll();
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

    public Ticket getTicketByIdWithDetails(Long id) {
        return ticketRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
    }

}
