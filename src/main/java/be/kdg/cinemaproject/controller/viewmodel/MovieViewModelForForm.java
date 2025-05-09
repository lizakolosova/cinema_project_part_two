package be.kdg.cinemaproject.controller.viewmodel;
import be.kdg.cinemaproject.domain.Genre;
import be.kdg.cinemaproject.domain.Movie;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MovieViewModelForForm {
    @NotBlank(message = "Title is required.")
    private String title;

    @NotNull(message = "Release date is required.")
    private LocalDate releaseDate;

    @NotNull(message = "Rating cannot be null")
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.")
    @DecimalMax(value = "10.0", message = "Rating must not exceed 10.")
    private Double rating;

    @NotNull(message = "Genre is required.")
    private Genre genre;

    @NotBlank(message = "Image name is required.")
    private String image;

    @Override
    public String toString() {
        return title + " (" + genre + ") - " + releaseDate + " - Rating: " + rating;
    }

    public Movie toMovie() {
        Movie movie = new Movie();
        movie.setTitle(this.title);
        movie.setReleaseDate(this.releaseDate);
        movie.setGenre(this.genre);
        movie.setRating(this.rating);
        movie.setImage(this.image);
        return movie;
    }

}
