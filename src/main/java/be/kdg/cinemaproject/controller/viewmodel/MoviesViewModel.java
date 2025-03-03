package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Movie;

import java.util.List;

public record MoviesViewModel(List<MovieViewModel> movies) {
    public static MoviesViewModel from( final List<Movie> movies) {
        final var moviesViewModel = movies.stream().map(MovieViewModel::from).toList();
        return new MoviesViewModel(moviesViewModel);
    }
}
