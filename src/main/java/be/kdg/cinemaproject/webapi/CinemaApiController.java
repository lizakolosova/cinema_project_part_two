package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.service.CinemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cinemas")
public class CinemaApiController {

    private final CinemaService cinemaService;

    public CinemaApiController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable("id") final Long id) {
        if (cinemaService.existsById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        cinemaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
