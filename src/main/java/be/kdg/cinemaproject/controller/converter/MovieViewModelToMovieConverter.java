package be.kdg.cinemaproject.controller.converter;

import be.kdg.cinemaproject.controller.viewmodel.MovieViewModelForForm;
import be.kdg.cinemaproject.domain.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MovieViewModelToMovieConverter implements Converter<MovieViewModelForForm, Movie> {
    @Override
    public Movie convert(MovieViewModelForForm source) {
        Movie movie = new Movie();
        movie.setTitle(source.getTitle());
        movie.setReleaseDate(source.getReleaseDate());
        movie.setGenre(source.getGenre());
        movie.setRating(source.getRating());
        movie.setImage(source.getImage());
        log.debug("MovieModelView is converted to Movie.");
        return movie;
    }
}
