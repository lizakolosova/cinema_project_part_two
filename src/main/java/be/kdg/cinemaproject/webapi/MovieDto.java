package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.domain.Movie;

public record MovieDto(Long id, String title, String releaseDate, String genre) {
    public static MovieDto fromMovie(final Movie movie) {
        return new MovieDto(movie.getId(), movie.getTitle(), movie.getReleaseDate().toString(), movie.getGenre().toString());
    }
}

