package be.kdg.cinemaproject.controller;

import be.kdg.cinemaproject.controller.viewmodel.TicketWithCinemasAndMoviesViewModel;
import be.kdg.cinemaproject.domain.Movie;
import be.kdg.cinemaproject.domain.Ticket;
import be.kdg.cinemaproject.domain.exception.MovieNotFoundException;
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

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping()
    public String getAll(Model model) {
        List<Ticket> tickets = ticketService.getAll();
        model.addAttribute("tickets", tickets);
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
        return new ModelAndView("ticket/addticket");
    }
}
