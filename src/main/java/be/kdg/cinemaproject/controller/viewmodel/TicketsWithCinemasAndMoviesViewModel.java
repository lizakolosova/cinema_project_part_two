package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Ticket;

import java.util.List;

public record TicketsWithCinemasAndMoviesViewModel(List<TicketWithCinemasAndMoviesViewModel> tickets) {
    public static TicketsWithCinemasAndMoviesViewModel from(final List<Ticket> tickets) {
        final var ticketsViewModel = tickets.stream().map(TicketWithCinemasAndMoviesViewModel::from).toList();
        return new TicketsWithCinemasAndMoviesViewModel(ticketsViewModel);
    }
}
