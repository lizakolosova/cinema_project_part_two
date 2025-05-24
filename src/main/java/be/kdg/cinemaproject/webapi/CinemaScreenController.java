package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.domain.CinemaScreen;
import be.kdg.cinemaproject.service.CinemaScreenService;
import be.kdg.cinemaproject.webapi.dto.cinemascreen.AddCinemaScreenDto;
import be.kdg.cinemaproject.webapi.dto.cinemascreen.CinemaScreenDto;
import be.kdg.cinemaproject.webapi.dto.cinemascreen.ScreenMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
public class CinemaScreenController {
    private final CinemaScreenService cinemaScreenService;

    private final ScreenMapper screenMapper;

    public CinemaScreenController(CinemaScreenService cinemaScreenService, ScreenMapper screenMapper) {
        this.cinemaScreenService = cinemaScreenService;
        this.screenMapper = screenMapper;
    }

    @GetMapping
    public ResponseEntity<List<CinemaScreenDto>> search(
            @RequestParam("title") final String title
    ) {
        final List<CinemaScreenDto> screens = cinemaScreenService.findByType(title)
                .stream()
                .map(CinemaScreenDto::fromCinemaScreen)
                .toList();
        return ResponseEntity.ok(screens);
    }
    @PostMapping
    public ResponseEntity<CinemaScreenDto> add(@RequestBody @Valid final AddCinemaScreenDto addCinemaScreenDto) {
        final CinemaScreen screen = cinemaScreenService.add(addCinemaScreenDto.screenNumber(), addCinemaScreenDto.screenType(), addCinemaScreenDto.size());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(screenMapper.toCinemaScreenDto(screen));
    }
}
