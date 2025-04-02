package be.kdg.cinemaproject.controller;
import be.kdg.cinemaproject.controller.viewmodel.MovieViewModelForForm;
import be.kdg.cinemaproject.controller.viewmodel.MoviesViewModel;
import be.kdg.cinemaproject.domain.Movie;
import be.kdg.cinemaproject.domain.exception.MovieNotFoundException;
import be.kdg.cinemaproject.controller.converter.MovieViewModelToMovieConverter;
import be.kdg.cinemaproject.service.MovieService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieViewModelToMovieConverter converter;

    public MovieController(MovieService movieService, MovieViewModelToMovieConverter converter) {
        this.movieService = movieService;
        this.converter = converter;
    }

    @GetMapping()
    public ModelAndView getAllMovies() {
        final ModelAndView modelAndView = new ModelAndView("movie/movies");
        modelAndView.addObject("movies", MoviesViewModel.from(movieService.getAllMovies()));
        return modelAndView;
    }

    @PostMapping("/filter")
    public ModelAndView getFilteredMovies(@RequestParam(value = "genre", required = false) String genreInput,
                                    @RequestParam(value = "rating", required = false) Double rating,
                                    Model model) {
        final ModelAndView modelAndView = new ModelAndView("movie/movies");
        List<Movie> movies = movieService.getFilteredMovies(genreInput, rating);
        modelAndView.addObject("movies", MoviesViewModel.from(movies));
        return modelAndView;
    }

    @GetMapping("/addmovie")
    public ModelAndView showAddMovieForm() {
        final ModelAndView modelAndView = new ModelAndView("movie/addmovie");
        log.info("Displaying form to add a new movie");
        modelAndView.addObject("movieViewModel", new MovieViewModelForForm());
        return modelAndView;
    }

    @PostMapping("/addmovie")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ModelAndView addMovie(
            @Valid @ModelAttribute("movieViewModel") MovieViewModelForForm movieViewModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final ModelAndView modelAndView = new ModelAndView("movie/addmovie");
            log.warn("Validation failed for movie: {}", bindingResult.getFieldErrors());
            return modelAndView;
        }
        final ModelAndView modelAndView = new ModelAndView("redirect:/movies");
        Movie movie = converter.convert(movieViewModel);
            movieService.addMovie(movie);
        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView viewMovieDetails(@PathVariable Long id) {
        try {
            final ModelAndView modelAndView = new ModelAndView("movie/movie-details");
            modelAndView.addObject("movie", movieService.findByIdWithCinemas(id));
            return modelAndView;
        } catch (MovieNotFoundException ex) {
            final ModelAndView modelAndView = new ModelAndView("error/other-error");
            log.error("Movie not found with id: {}", id);
            modelAndView.addObject("message", ex.getMessage());
            modelAndView.addObject("condition", ex.getCondition());
            modelAndView.addObject("time",  ex.getTime());
            return modelAndView;
        }
    }

    @GetMapping("/find")
    public String findMovie() {
        return "movie/movies-by-title";
    }
}

