package be.kdg.cinemaproject.controller;

import be.kdg.cinemaproject.domain.Cinema;
import be.kdg.cinemaproject.domain.exception.CinemaNotFoundException;
import be.kdg.cinemaproject.controller.converter.CinemaViewModelToCinemaConverter;
import be.kdg.cinemaproject.controller.viewmodel.CinemaViewModelForForm;
import be.kdg.cinemaproject.service.CinemaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("cinemas")
public class CinemaController {

    private static final Logger logger = LoggerFactory.getLogger(CinemaController.class);

    private final CinemaService cinemaService;
    private final CinemaViewModelToCinemaConverter converter;

    public CinemaController(CinemaService cinemaService, CinemaViewModelToCinemaConverter converter) {
        this.cinemaService = cinemaService;
        this.converter = converter;

    }

    @GetMapping()
    public String getAllCinemas(Model model) {
        logger.info("Fetching all cinemas");
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        logger.debug("Number of cinemas fetched: {}", cinemas.size());
        model.addAttribute("cinemas", cinemas);  //TO DO: make this controller method addAttribute with View model
        return "cinema/cinemas";
    }

    @PostMapping("/filter")
    public String getCinemasByCapacity(@RequestParam("minCapacity") int minCapacity, Model model) {
        logger.info("Filtering cinemas by minimum capacity: {}", minCapacity);
        List<Cinema> filteredCinemas = cinemaService.getCinemasByCapacity(minCapacity);
        logger.debug("Number of cinemas after filtering: {}", filteredCinemas.size());
        model.addAttribute("cinemas", filteredCinemas);  //TO DO: make this controller method should addAttribute with View model
        return "cinema/cinemas";
    }

    @GetMapping("/addcinema")
    public String showAddCinemaForm(Model model) {
        model.addAttribute("cinemaViewModel", new CinemaViewModelForForm());
        return "cinema/addcinema";
    }

    @PostMapping("/addcinema")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public String addCinema(
            @Valid @ModelAttribute("cinemaViewModel") CinemaViewModelForForm cinemaViewModel,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            logger.warn("Form submission contains validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("cinemaViewModel", cinemaViewModel);
            return "cinema/addcinema";
        }
        logger.info("Processing cinema addition request: {}", cinemaViewModel.getName());

        Cinema cinema = converter.convert(cinemaViewModel);
        cinemaService.saveCinema(cinema);
        return "redirect:/cinemas";
    }
    @GetMapping("/details/{id}")
    public String viewCinemaDetails(@PathVariable Long id, Model model) {
        try {
            Cinema cinema = cinemaService.findByIdWithMovies(id);
            model.addAttribute("cinema", cinema);  //TO DO: make this controller method should addAttribute with View model
            return "cinema/cinema-details";
        } catch (CinemaNotFoundException ex) {
            logger.error("Cinema not found with id: {}", id);
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("condition", ex.getCondition());
            model.addAttribute("time",  ex.getTime());
            return "error/other-error";
        }
    }
}
