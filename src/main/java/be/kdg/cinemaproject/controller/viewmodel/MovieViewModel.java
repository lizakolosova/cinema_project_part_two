package be.kdg.cinemaproject.controller.viewmodel;

import be.kdg.cinemaproject.domain.Genre;
import be.kdg.cinemaproject.domain.Movie;

import java.time.LocalDate;

public record MovieViewModel(Long id,
                             String title,
                             LocalDate releaseDate,
                             Double rating,
                             Genre genre,
                             String image) {
    public static MovieViewModel from(final Movie movie) {
        return new MovieViewModel(movie.getId(), movie.getTitle(), movie.getReleaseDate(), movie.getRating(), movie.getGenre(), movie.getImage());
    }
}
