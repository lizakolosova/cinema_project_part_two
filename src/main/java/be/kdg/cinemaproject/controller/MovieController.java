package be.kdg.cinemaproject.controller;
import be.kdg.cinemaproject.domain.Movie;
import be.kdg.cinemaproject.domain.exception.MovieNotFoundException;
import be.kdg.cinemaproject.controller.converter.MovieViewModelToMovieConverter;
import be.kdg.cinemaproject.controller.viewmodel.MovieViewModel;
import be.kdg.cinemaproject.service.MovieService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class MovieController {

    private final MovieService movieService;
    private final MovieViewModelToMovieConverter converter;

    @Autowired
    public MovieController(MovieService movieService, MovieViewModelToMovieConverter converter) {
        this.movieService = movieService;
        this.converter = converter;
    }

    @GetMapping("/movies")
    public String getAllMovies(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "movies";
    }

    @PostMapping("/movies/filter")
    public String getFilteredMovies(@RequestParam(value = "genre", required = false) String genreInput,
                                    @RequestParam(value = "rating", required = false) Double rating,
                                    Model model) {
        List<Movie> movies = movieService.getFilteredMovies(genreInput, rating);
        model.addAttribute("movies", movies);
        return "movies";
    }

    @GetMapping("/addmovie")
    public String showAddMovieForm(Model model) {
        log.info("Displaying form to add a new movie");
        model.addAttribute("movieViewModel", new MovieViewModel());
        return "addmovie";
    }

    @PostMapping("/addmovie")
    public String addMovie(
            @Valid @ModelAttribute("movieViewModel") MovieViewModel movieViewModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed for movie: {}", bindingResult.getFieldErrors());
            return "addmovie";
        }
        Movie movie = converter.convert(movieViewModel);
            movieService.addMovie(movie);
        return "redirect:/movies";
    }

    @GetMapping("/movie-details/{id}")
    public String viewMovieDetails(@PathVariable Long id, Model model) {
        try {
            Movie movie = movieService.findByIdWithCinemas(id);
            model.addAttribute("movie", movie);
            return "movie-details";
        } catch (MovieNotFoundException ex) {
            log.error("Movie not found with id: {}", id);
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("condition", ex.getCondition());
            model.addAttribute("time",  ex.getTime());
            return "other-error";
        }
    }

    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteById(id);
        return "redirect:/movies";
    }
}

