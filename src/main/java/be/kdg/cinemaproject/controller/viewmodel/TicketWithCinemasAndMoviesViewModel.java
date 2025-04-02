package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Availability;
import be.kdg.cinemaproject.domain.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public record TicketWithCinemasAndMoviesViewModel(
            Long id,
            Long movieId,
            String movieTitle,
            Long cinemaId,
            String cinemaName,
            LocalDateTime showtime,
            String format,
            double price,
            Availability availability,
            String image,
            VisitorViewModel visitor,
            boolean isModificationAllowed
    ) {
    public static TicketWithCinemasAndMoviesViewModel from(final Ticket ticket, final boolean isModificationAllowed) {
        final VisitorViewModel visitor = new VisitorViewModel(ticket.getVisitor().getId(), ticket.getVisitor().getName(), ticket.getVisitor().getEmail());
        return new TicketWithCinemasAndMoviesViewModel(ticket.getId(), ticket.getMovie().getId(), ticket.getMovie().getTitle(),
                ticket.getCinema().getId(), ticket.getCinema().getName(), ticket.getShowtime(), ticket.getFormat(),
                ticket.getPrice(), ticket.getAvailability(), ticket.getImage(), visitor,
                isModificationAllowed);
    }
}
