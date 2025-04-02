package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Ticket;

import java.util.List;

public record TicketsWithCinemasAndMoviesViewModel(List<TicketWithCinemasAndMoviesViewModel> tickets) {

}
