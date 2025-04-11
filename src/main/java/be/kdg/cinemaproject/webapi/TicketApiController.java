package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.domain.Ticket;
import be.kdg.cinemaproject.domain.exception.TicketNotFoundException;
import be.kdg.cinemaproject.service.TicketService;
import be.kdg.cinemaproject.webapi.dto.ticket.PatchTicketDto;
import be.kdg.cinemaproject.webapi.dto.ticket.TicketDto;
import be.kdg.cinemaproject.webapi.dto.ticket.TicketMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tickets")
public class TicketApiController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    public TicketApiController(TicketService ticketService, TicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canModifyTicket(principal, #id)")
    public ResponseEntity<TicketDto> patch(@PathVariable("id") final Long id,
                                           @RequestBody @Valid final PatchTicketDto patchTicketDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ticket data");
        } else {
            final Ticket ticket =
                    ticketService.patch(id, patchTicketDto.price(), patchTicketDto.showtime(), patchTicketDto.availability());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ticketMapper.toTicketDto(ticket));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.canModifyTicket(principal, #id)")
    public ResponseEntity<Void> remove(
            @PathVariable("id") final Long id) {
        try {
            ticketService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (TicketNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
