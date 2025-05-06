package be.kdg.cinemaproject.controller;

import be.kdg.cinemaproject.controller.viewmodel.CinemasViewModel;
import be.kdg.cinemaproject.controller.viewmodel.MoviesViewModel;
import be.kdg.cinemaproject.controller.viewmodel.TicketWithCinemasAndMoviesViewModel;
import be.kdg.cinemaproject.controller.viewmodel.TicketsWithCinemasAndMoviesViewModel;
import be.kdg.cinemaproject.security.CustomUserDetails;
import be.kdg.cinemaproject.service.AuthorizationService;
import be.kdg.cinemaproject.service.CinemaService;
import be.kdg.cinemaproject.service.MovieService;
import be.kdg.cinemaproject.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/tickets")
@Slf4j
public class TicketController {

    private final TicketService ticketService;
    private final MovieService movieService;
    private final CinemaService cinemaService;
    private final AuthorizationService authorizationService;

    public TicketController(TicketService ticketService, MovieService movieService, CinemaService cinemaService, AuthorizationService authorizationService) {
        this.ticketService = ticketService;
        this.movieService = movieService;
        this.cinemaService = cinemaService;
        this.authorizationService = authorizationService;
    }

    @GetMapping()
    public ModelAndView getAll(@AuthenticationPrincipal final CustomUserDetails userDetails,
                               HttpServletRequest request) {
        final ModelAndView modelAndView = new ModelAndView("ticket/tickets");
        modelAndView.addObject(
                "tickets",
                new TicketsWithCinemasAndMoviesViewModel(ticketService.getAll()
                        .stream()
                        .map(ticket -> TicketWithCinemasAndMoviesViewModel.from(ticket, authorizationService.canModifyTicket(userDetails, ticket)))
                        .toList())
        );
        return modelAndView;
    }

    @GetMapping("/details/{id}")
    public ModelAndView getDetails(@PathVariable Long id,
                                   @AuthenticationPrincipal final CustomUserDetails userDetails) {
        final ModelAndView modelAndView = new ModelAndView("ticket/ticket-details");
        modelAndView.addObject("ticket", TicketWithCinemasAndMoviesViewModel.from(
                ticketService.getTicketByIdWithDetails(id).orElseThrow(),
                authorizationService.canModifyTicket(userDetails, id)));
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
