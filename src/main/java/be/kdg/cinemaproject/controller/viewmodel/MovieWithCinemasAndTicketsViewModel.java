package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Cinema;
import be.kdg.cinemaproject.domain.Genre;
import be.kdg.cinemaproject.domain.Movie;
import be.kdg.cinemaproject.domain.Ticket;

import java.time.LocalDate;
import java.util.List;

public record MovieWithCinemasAndTicketsViewModel(Long id,
                                                  String title,
                                                  LocalDate releaseDate,
                                                  Double rating,
                                                  Genre genre,
                                                  String image, List<Ticket> tickets, List<Cinema> cinemas) {
    public static MovieWithCinemasAndTicketsViewModel from(final Movie movie) {
        return new MovieWithCinemasAndTicketsViewModel(movie.getId(), movie.getTitle(), movie.getReleaseDate(), movie.getRating(), movie.getGenre(), movie.getImage(), movie.getTickets(), movie.getTickets().stream().map(Ticket::getCinema).toList());
    }
}
