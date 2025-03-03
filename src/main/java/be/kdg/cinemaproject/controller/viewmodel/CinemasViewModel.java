package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Cinema;
import java.util.List;

public record CinemasViewModel(List<CinemaViewModel> cinemas) {
    public static CinemasViewModel from( final List<Cinema> cinemas) {
        final var cinemasViewModel = cinemas.stream().map(CinemaViewModel::from).toList();
        return new CinemasViewModel(cinemasViewModel);
    }
}
