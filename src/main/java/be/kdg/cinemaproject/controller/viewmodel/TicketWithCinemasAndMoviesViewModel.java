package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Availability;
import be.kdg.cinemaproject.domain.Ticket;

import java.time.LocalDateTime;

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
            String image
    ) {
    public static TicketWithCinemasAndMoviesViewModel from(final Ticket ticket) {
        return new TicketWithCinemasAndMoviesViewModel(ticket.getId(),ticket.getMovie().getId(), ticket.getMovie().getTitle(),ticket.getCinema().getId() ,ticket.getCinema().getName(),
                ticket.getShowtime(), ticket.getFormat(), ticket.getPrice(), ticket.getAvailability(), ticket.getImage());
    }
}
