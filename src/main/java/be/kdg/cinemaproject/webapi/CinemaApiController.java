package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.domain.Ticket;
import be.kdg.cinemaproject.security.CustomUserDetails;
import be.kdg.cinemaproject.service.CinemaService;
import be.kdg.cinemaproject.service.TicketService;
import be.kdg.cinemaproject.webapi.dto.AddTicketDto;
import be.kdg.cinemaproject.webapi.dto.TicketDto;
import be.kdg.cinemaproject.webapi.dto.TicketMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/cinemas")
public class CinemaApiController {

    private final CinemaService cinemaService;
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    public CinemaApiController(CinemaService cinemaService, TicketService ticketService, TicketMapper ticketMapper) {
        this.cinemaService = cinemaService;
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> remove(@PathVariable("id") final Long id) {
        if (cinemaService.existsById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        cinemaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tickets")
    @PreAuthorize("@authorizationService.canModifyTicket(principal, #id)")
    public ResponseEntity<TicketDto> add(@PathVariable Long id,
                                         @RequestBody @Valid final AddTicketDto addTicketDto,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal final CustomUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ticket data");
        }

        final Ticket ticket = ticketService.add(addTicketDto.price(), addTicketDto.showtime(), addTicketDto.format(), addTicketDto.availability(), addTicketDto.image(), addTicketDto.movieId(), id, userDetails.getVisitorId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketMapper.toTicketDto(ticket));
    }
}
