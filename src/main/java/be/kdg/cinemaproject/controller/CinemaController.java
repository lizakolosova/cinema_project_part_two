package be.kdg.cinemaproject.controller;

import be.kdg.cinemaproject.domain.Cinema;
import be.kdg.cinemaproject.domain.exception.CinemaNotFoundException;
import be.kdg.cinemaproject.controller.converter.CinemaViewModelToCinemaConverter;
import be.kdg.cinemaproject.controller.viewmodel.CinemaViewModel;
import be.kdg.cinemaproject.service.CinemaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CinemaController {

    private static final Logger logger = LoggerFactory.getLogger(CinemaController.class);

    private final CinemaService cinemaService;
    private final CinemaViewModelToCinemaConverter converter;

    @Autowired
    public CinemaController(CinemaService cinemaService, CinemaViewModelToCinemaConverter converter) {
        this.cinemaService = cinemaService;
        this.converter = converter;

    }

    @GetMapping("/cinemas")
    public String getAllCinemas(Model model) {
        logger.info("Fetching all cinemas");
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        logger.debug("Number of cinemas fetched: {}", cinemas.size());
        model.addAttribute("cinemas", cinemas);
        return "cinemas";
    }

    @PostMapping("/cinemas/filter")
    public String getCinemasByCapacity(@RequestParam("minCapacity") int minCapacity, Model model) {
        logger.info("Filtering cinemas by minimum capacity: {}", minCapacity);
        List<Cinema> filteredCinemas = cinemaService.getCinemasByCapacity(minCapacity);
        logger.debug("Number of cinemas after filtering: {}", filteredCinemas.size());
        model.addAttribute("cinemas", filteredCinemas);
        return "cinemas";
    }

    @GetMapping("/addcinema")
    public String showAddCinemaForm(Model model) {
        model.addAttribute("cinemaViewModel", new CinemaViewModel());
        return "addcinema";
    }

    @PostMapping("/addcinema")
    public String addCinema(
            @Valid @ModelAttribute("cinemaViewModel") CinemaViewModel cinemaViewModel,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            logger.warn("Form submission contains validation errors: {}", bindingResult.getAllErrors());
            model.addAttribute("cinemaViewModel", cinemaViewModel);
            return "addcinema";
        }
        logger.info("Processing cinema addition request: {}", cinemaViewModel.getName());

        Cinema cinema = converter.convert(cinemaViewModel);
        cinemaService.saveCinema(cinema);
        return "redirect:/cinemas";
    }
    @GetMapping("/cinema-details/{id}")
    public String viewCinemaDetails(@PathVariable Long id, Model model) {
        try {
            Cinema cinema = cinemaService.findByIdWithMovies(id);
            model.addAttribute("cinema", cinema);
            return "cinema-details";
        } catch (CinemaNotFoundException ex) {
            logger.error("Cinema not found with id: {}", id);
            model.addAttribute("message", ex.getMessage());
            model.addAttribute("condition", ex.getCondition());
            model.addAttribute("time",  ex.getTime());
            return "other-error";
        }
    }
    @PostMapping("/cinemas/delete/{id}")
    public String deleteCinema(@PathVariable Long id) {
        cinemaService.deleteById(id);
        return "redirect:/cinemas";
    }

    @GetMapping("/address")
    public String searchByAddressForm(@RequestParam(required = false) String address, Model model) {
            model.addAttribute("cinemas", cinemaService.findCinemasByAddress(address));
        return "cinemas-by-address";
    }

    @PostMapping("/address")
    public String searchByAddress(@RequestParam("address") String address, Model model) {
        model.addAttribute("cinemas", cinemaService.findCinemasByAddress(address));
        logger.info("Fetching cinemas by address: {}", address);
        return "cinemas-by-address";
    }
}
