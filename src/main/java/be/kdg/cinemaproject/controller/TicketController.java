package be.kdg.cinemaproject.controller;

import be.kdg.cinemaproject.controller.viewmodel.CinemasViewModel;
import be.kdg.cinemaproject.controller.viewmodel.MoviesViewModel;
import be.kdg.cinemaproject.controller.viewmodel.TicketWithCinemasAndMoviesViewModel;
import be.kdg.cinemaproject.controller.viewmodel.TicketsWithCinemasAndMoviesViewModel;
import be.kdg.cinemaproject.domain.Ticket;
import be.kdg.cinemaproject.service.CinemaService;
import be.kdg.cinemaproject.service.MovieService;
import be.kdg.cinemaproject.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final MovieService movieService;
    private final CinemaService cinemaService;

    public TicketController(TicketService ticketService, MovieService movieService, CinemaService cinemaService) {
        this.ticketService = ticketService;
        this.movieService = movieService;
        this.cinemaService = cinemaService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("tickets", TicketsWithCinemasAndMoviesViewModel.from(ticketService.getAll()));
        return "ticket/tickets";
    }

    @GetMapping("/details/{id}")
    public ModelAndView getDetails(@PathVariable Long id) {
        final ModelAndView modelAndView = new ModelAndView("ticket/ticket-details");
        modelAndView.addObject("ticket", TicketWithCinemasAndMoviesViewModel.from(ticketService.getTicketByIdWithDetails(id)));
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView add() {
        final ModelAndView modelAndView = new ModelAndView("ticket/addticket");
        modelAndView.addObject("movies", MoviesViewModel.from(movieService.getAllMovies()));
        modelAndView.addObject("cinemas", CinemasViewModel.from(cinemaService.getAllCinemas()));
        return modelAndView;
    }
}
