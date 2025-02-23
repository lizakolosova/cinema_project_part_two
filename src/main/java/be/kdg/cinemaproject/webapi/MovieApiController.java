package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.service.MovieService;
import be.kdg.cinemaproject.webapi.dto.MovieDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieApiController {

    private final MovieService movieService;

    public MovieApiController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> search(
            @RequestParam("title") final String title
    ) {
        final List<MovieDto> movies = movieService.findByTitle(title)
                .stream()
                .map(MovieDto::fromMovie)
                .toList();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(movies);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable("id") final Long id) {
        if (movieService.existsById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        movieService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
 }

