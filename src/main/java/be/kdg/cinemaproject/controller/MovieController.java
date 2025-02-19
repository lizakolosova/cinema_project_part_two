package be.kdg.cinemaproject.controller;
import be.kdg.cinemaproject.controller.viewmodel.MovieViewModelForForm;
import be.kdg.cinemaproject.controller.viewmodel.MoviesViewModel;
import be.kdg.cinemaproject.domain.Movie;
import be.kdg.cinemaproject.domain.exception.MovieNotFoundException;
import be.kdg.cinemaproject.controller.converter.MovieViewModelToMovieConverter;
import be.kdg.cinemaproject.service.MovieService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
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
    public String getFilteredMovies(@RequestParam(value = "genre", required = false) String genreInput,
                                    @RequestParam(value = "rating", required = false) Double rating,
                                    Model model) {
        List<Movie> movies = movieService.getFilteredMovies(genreInput, rating);
        model.addAttribute("movies", MoviesViewModel.from(movies));
        return "movie/movies";
    }

    @GetMapping("/addmovie")
    public String showAddMovieForm(Model model) {
        log.info("Displaying form to add a new movie");
        model.addAttribute("movieViewModel", new MovieViewModelForForm());
        return "movie/addmovie";
    }

    @PostMapping("/addmovie")
    public String addMovie(
            @Valid @ModelAttribute("movieViewModel") MovieViewModelForForm movieViewModel,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation failed for movie: {}", bindingResult.getFieldErrors());
            return "movie/addmovie";
        }
        Movie movie = converter.convert(movieViewModel);
            movieService.addMovie(movie);
        return "redirect:/movies";
    }

    @GetMapping("/details/{id}")
    public String viewMovieDetails(@PathVariable Long id, Model model) {
        try {
            Movie movie = movieService.findByIdWithCinemas(id);
            model.addAttribute("movie", movie); //TO DO: make this controller method should addAttribute with View model
            return "movie/movie-details";
        } catch (MovieNotFoundException ex) {
            log.error("Movie not found with id: {}", id);
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("condition", ex.getCondition());
            model.addAttribute("time",  ex.getTime());
            return "error/other-error";
        }
    }

    @GetMapping("/find")
    public String findMovie(Model model) {
        return "movie/movies-by-title";
    }
}

