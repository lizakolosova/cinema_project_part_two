package be.kdg.cinemaproject.controller;

import be.kdg.cinemaproject.controller.viewmodel.CinemaWithScreensViewModel;
import be.kdg.cinemaproject.controller.viewmodel.CinemasViewModel;
import be.kdg.cinemaproject.domain.Cinema;
import be.kdg.cinemaproject.domain.exception.CinemaNotFoundException;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("cinemas")
public class CinemaController {

    private static final Logger logger = LoggerFactory.getLogger(CinemaController.class);

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping()
    public ModelAndView getAllCinemas() {
        ModelAndView modelAndView = new ModelAndView("cinema/cinemas");
        logger.info("Fetching all cinemas");
        modelAndView.addObject("cinemas", CinemasViewModel.from(cinemaService.getAllCinemas()));
        return modelAndView;
    }

    @PostMapping("/filter")
    public ModelAndView getCinemasByCapacity(@RequestParam("minCapacity") int minCapacity) {
        ModelAndView modelAndView = new ModelAndView("cinema/cinemas");
        logger.info("Filtering cinemas by minimum capacity: {}", minCapacity);
        modelAndView.addObject("cinemas", CinemasViewModel.from(cinemaService.getCinemasByCapacity(minCapacity)));
        return modelAndView;
    }

    @GetMapping("/addcinema")
    public String showAddCinemaForm(Model model) {
        model.addAttribute("cinemaViewModel", new CinemaViewModelForForm());
        return "cinema/addcinema";
    }

    @PostMapping("/addcinema")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ModelAndView addCinema(
            @Valid @ModelAttribute("cinemaViewModel") CinemaViewModelForForm cinemaViewModel,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("cinema/addcinema");
            logger.warn("Form submission contains validation errors: {}", bindingResult.getAllErrors());
            modelAndView.addObject("cinemaViewModel", cinemaViewModel);
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/cinemas");
        logger.info("Processing cinema addition request: {}", cinemaViewModel.getName());

        Cinema cinema = cinemaViewModel.toCinema();
        cinemaService.saveCinema(cinema);
        return modelAndView;
    }
    @GetMapping("/details/{id}")
    public ModelAndView viewCinemaDetails(@PathVariable Long id) {
        try {
            ModelAndView modelAndView = new ModelAndView("cinema/cinema-details");
            modelAndView.addObject("cinema", CinemaWithScreensViewModel.from(cinemaService.findByIdWithMovies(id)));
            return modelAndView;
        } catch (CinemaNotFoundException ex) {
            ModelAndView modelAndView = new ModelAndView( "error/other-error");
            logger.error("Cinema not found with id: {}", id);
            modelAndView.addObject("message", ex.getMessage());
            modelAndView.addObject("condition", ex.getCondition());
            modelAndView.addObject("time",  ex.getTime());
            return modelAndView;
        }
    }
}
